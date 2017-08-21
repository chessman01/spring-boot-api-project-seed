package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.*;
import com.tianbao.buy.manager.OrderMainManager;
import com.tianbao.buy.service.*;
import com.tianbao.buy.utils.MakeOrderNum;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService{
    @Resource
    private OrderMainManager orderManager;

    @Resource
    private UserService userService;

    @Resource
    private YenCardService cardService;

    @Resource
    private CourseService courseService;

    @Resource
    private CouponService couponService;

    @Resource
    private FundDetailService fundDetailService;

    @Override
    public OrderVO build(long courseId) {
        return render(courseId, null, null, 0, new Context());
    }

    @Override
    public OrderVO adjust(long courseId, Long cardId, Long couponId, int personTime) {
        return render(courseId, cardId, couponId, personTime, new Context());
    }

    @Override
    public String create(long courseId, long couponId, int personTime, long cardId) {
        Context context = new Context();
        OrderVO orderVO = render(courseId, cardId, couponId, personTime, context);
        String orderId = MakeOrderNum.makeOrderNum();
        List<OrderVO.PayDetail> payDetails= orderVO.getPayDetail();

        int realFee = 0, cardDiscountFee = 0, couponFee = 0, totalFee = 0, cardFee = 0;


        for (OrderVO.PayDetail payDetail : payDetails) {
            if (payDetail.getTitle().equals(TOTAL_FEE)) {
                totalFee = payDetail.getOriginFee();
                continue;
            }

            if (payDetail.getTitle().equals(COUPON_FEE)) {
                couponFee = payDetail.getOriginFee();
                continue;
            }

            if (payDetail.getTitle().equals(CARD_DISCOUNT)) {
                cardDiscountFee = payDetail.getOriginFee();
                continue;
            }

            if (payDetail.getTitle().equals(CARD_PAY_FEE)) {
                cardFee = payDetail.getOriginFee();
                continue;
            }
        }

        realFee = orderVO.getRealPay().getOriginFee();

        fundDetailService.initFund4PerIn(orderId, realFee, cardFee, couponFee, new Date());

        // 生成订单
        OrderMain order = convert(orderId, context.getUser().getId(), cardId, realFee, context.getTemplate().getRulePrice(),
                0, 0, cardId, 0, "0", couponFee, context.getCouponUser().getId(), 0, OrderVO.Status.PENDING.getCode());

        sava(order);

        // 礼券要锁定
        couponService.updateCouponUserStatus(context.getCouponUser().getId(), CouponVO.Status.PENDING.getCode(),
                CouponVO.Status.NORMAL.getCode());

        return "weixin url";
    }

    private OrderVO render(long courseId, Long cardId, Long couponId, int personTime, Context context) {
        OrderVO order = new OrderVO();

        // 1. 用户信息，并且必须是填写手机号
        User user = userService.getUserByWxUnionId();

        if (StringUtils.isBlank(user.getPhone())) throw new BizException("请校验手机号码");

        // 2. 获取瘾卡信息
        YenCard card;
        if (cardId != null && cardId > NumberUtils.LONG_ZERO) {
            card = cardService.getSpecify(user.getId(), cardId);
        } else {
            card = cardService.getDefault(user.getId());
        }

        order.setCard(cardService.convert2CardVO(card));

        // 3. 获取课程信息
        Course course = courseService.getNormalCourse().get(courseId);
        if (course == null) throw new BizException("没找到有效课程");
        order.setCourse(courseService.convert2CourseVO(course, false));

        context.setUser(user);
        context.setCard(card);
        context.setCourse(course);

        // 4. 找到礼券
        List<CouponVO> couponVOs = couponService.getCoupon4PayPerView(user.getId(),
                course.getPrice(), couponId, context);
        order.setCoupon(couponVOs);

        // 5. 人次
        List<OrderVO.PersonTime> personTimes = Lists.newArrayList();
        if (personTime > 3 || personTime < 1) personTime = 1;

        personTimes.add( new OrderVO.PersonTime("1人", 1, personTime == 1 ? true : false));
        personTimes.add( new OrderVO.PersonTime("2人", 2, personTime == 2 ? true : false));
        personTimes.add( new OrderVO.PersonTime("3人", 3, personTime == 3 ? true : false));
        order.setPersonTime(personTimes);

        // 6. 支付信息
        List<OrderVO.PayDetail> payDetails = Lists.newArrayList();
        Map<String, OrderVO.PayDetail> payDetailMap = calFeeDetail(course, personTime, card,
                context.getCoupon(), payDetails);
        order.setPayDetail(payDetails);

        OrderVO.PayDetail realPay = calRealPay(payDetailMap);
        order.setRealPay(realPay);

        // 4. 按钮
        Button button = new Button();
        button.setEvent(new Button.Event("http://h5.m.taobao.com", "click"));
        button.setTitle("支付（￥" + realPay.getFee() + "）");


        order.setButton(button);

        return order;
    }

    private OrderVO.PayDetail calRealPay(Map<String, OrderVO.PayDetail> map) {
        int total = map.get("total").getOriginFee();
        int couponDiscount = map.get("couponDisCount") != null ? map.get("couponDisCount").getOriginFee() : NumberUtils.INTEGER_ZERO;
        int cardDiscount = map.get("cardDiscount") != null ? map.get("cardDiscount").getOriginFee() : NumberUtils.INTEGER_ZERO;
        int cardPay = map.get("cardPay") != null ? map.get("cardPay").getOriginFee() : NumberUtils.INTEGER_ZERO;

        /* 实付款 */
        int realPayFee = total - couponDiscount - cardDiscount - cardPay;

        return new OrderVO.PayDetail(REAL_PAY_FEE, MoneyUtils.unitFormat(2, realPayFee), realPayFee);
    }

    private Map<String, OrderVO.PayDetail> calFeeDetail(Course course, int personTime, YenCard card, CouponTemplate coupon,
                                                        List<OrderVO.PayDetail> payDetails) {
        Map<String, OrderVO.PayDetail> map = Maps.newHashMap();
        OrderVO.PayDetail totalDetail, couponDisCountDetail, cardDiscountDetail, cardPayDetail;

        /* 课程总价 */
        int total = course.getPrice() * personTime;
        totalDetail = new OrderVO.PayDetail(TOTAL_FEE, MoneyUtils.unitFormat(2, total), total);
        payDetails.add(totalDetail);
        map.put("total", totalDetail);

         /* 礼券 */
        int couponDiscount = 0;
        if (coupon != null) {
            couponDiscount = coupon.getPrice();
            couponDisCountDetail = new OrderVO.PayDetail(COUPON_FEE, MoneyUtils.minusUnitFormat(2, couponDiscount), couponDiscount);
            payDetails.add(couponDisCountDetail);
            map.put("couponDisCount", couponDisCountDetail);
        }

        /* 瘾卡优惠 */
        int cardDiscount = 0;
        if (card.getCashAccount() + card.getGiftAccount() >= (total - couponDiscount) * card.getDiscountRate() / 100) {
            cardDiscount = total - couponDiscount - (total - couponDiscount) * card.getDiscountRate() / 100;
            cardDiscountDetail = new OrderVO.PayDetail(CARD_DISCOUNT, MoneyUtils.minusUnitFormat(2, cardDiscount), cardDiscount);
            payDetails.add(cardDiscountDetail);
            map.put("cardDiscount", cardDiscountDetail);
        }

        /* 瘾卡支付 */
        int cardPay;

        if (card.getCashAccount() + card.getGiftAccount() > total - couponDiscount - cardDiscount) {
            cardPay = total - couponDiscount - cardDiscount;
            cardPayDetail = new OrderVO.PayDetail(CARD_PAY_FEE, MoneyUtils.minusUnitFormat(2, cardPay), cardPay);
            payDetails.add(cardPayDetail);
            map.put("cardPay", cardPayDetail);
        } else if (card.getCashAccount() + card.getGiftAccount() > 0) {
            cardPay = card.getCashAccount() + card.getGiftAccount();
            cardPayDetail = new OrderVO.PayDetail(CARD_PAY_FEE, MoneyUtils.minusUnitFormat(2, cardPay), cardPay);
            payDetails.add(cardPayDetail);
            map.put("cardPay", cardPayDetail);
        }

        return map;
    }


    public void sava (OrderMain order) {
        orderManager.save(order);
    }

    private void updateStatus(Long orderId, OrderVO.Status status, OrderVO.Status oldStatus,
                              Date payTime, String payOrderId) {
        Condition condition = new Condition(OrderMain.class);

        condition.createCriteria().andCondition("order_id=", orderId)
                .andCondition("status=", oldStatus.getCode());

        OrderMain order = new OrderMain();
        order.setStatus(status.getCode());
        order.setPayTime(payTime);
        order.setPayOrderId(payOrderId);

        orderManager.update(order, condition);
    }

    @Override
    public OrderMain convert(String orderId, Long userId, Long classId, Integer realPay, Integer totalPrice,
                          Integer yenCarPayPrice, Integer yenCarDiscount, Long yenCarId, Integer onlineDiscount,
                          String onlineRule, Integer couponDiscount, Long couponId, Integer giftDiscount, Byte status) {
        OrderMain order = new OrderMain();

        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setClassId(classId);
        order.setRealPay(realPay);
        order.setTotalPrice(totalPrice);
        order.setYenCarPayPrice(yenCarPayPrice);
        order.setYenCarDiscount(yenCarDiscount);
        order.setYenCarId(yenCarId);
        order.setOnlineDiscount(onlineDiscount);
        order.setOnlineRule(onlineRule);
        order.setCouponDiscount(couponDiscount);
        order.setCouponId(couponId);
        order.setGiftDiscount(giftDiscount);
        order.setStatus(status);

        return order;
    }
}
