package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.*;
import com.tianbao.buy.manager.OrderMainManager;
import com.tianbao.buy.service.*;
import com.tianbao.buy.utils.MakeOrderNum;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Value("${biz.online.reduce.fee}")
    public static Integer onlineReduceFee;

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
    public List<OrderVO> get(byte status) {
        User user = userService.getUserByWxUnionId();

        Condition condition = new Condition(OrderMain.class);
        condition.orderBy("createTime");
        condition.createCriteria().andEqualTo("status", status).andCondition("user_id=", user.getId())
                .andCondition("type=", OrderVO.Type.COURSE.getCode());

        List<OrderMain> orderMains = orderManager.findByCondition(condition);

        Set<Long> courseIds = Sets.newHashSet();

        for (OrderMain orderMain : orderMains) {
            courseIds.add(orderMain.getClassId());
        }

        Map<Long, Course> courseMap = courseService.getCourse(courseIds);

        List<OrderVO> orderVOs = Lists.newArrayList();

        for (OrderMain orderMain : orderMains) {
            OrderVO orderVO = convert2OrderVO(orderMain, courseMap.get(orderMain.getClassId()));
            orderVOs.add(orderVO);
        }

        return orderVOs;
    }

    private OrderVO convert2OrderVO(OrderMain orderMain, Course course) {
        checkNotNull(orderMain);
        checkNotNull(course);

        OrderVO order = new OrderVO();

        /* 实付款 */
        int realPayFee = orderMain.getRealPay();
        int yenCarPayPrice = orderMain.getYenCarPayPrice();
        int couponDiscount = orderMain.getCouponDiscount();
//        int onlineDiscount = orderMain.getOnlineDiscount();
        int yenCarDiscount = orderMain.getYenCarDiscount();

        List<OrderVO.PayDetail> payDetails = Lists.newArrayList();
        order.setRealPay(new OrderVO.PayDetail(REAL_PAY_FEE, MoneyUtils.unitFormat(2, realPayFee / 100), realPayFee));
        payDetails.add(new OrderVO.PayDetail(TOTAL_FEE, MoneyUtils.unitFormat(2, course.getPrice() * orderMain.getPersonTime() / 100),
                course.getPrice() * orderMain.getPersonTime()));
        if (yenCarDiscount > 0) payDetails.add(new OrderVO.PayDetail(CARD_DISCOUNT, MoneyUtils.unitFormat(2, yenCarDiscount / 100), yenCarDiscount));
//        if (onlineDiscount > 0) payDetails.add(new OrderVO.PayDetail(CARD_PAY_FEE, MoneyUtils.unitFormat(2, onlineDiscount / 100), onlineDiscount));
        if (couponDiscount > 0) payDetails.add(new OrderVO.PayDetail(COUPON_FEE, MoneyUtils.unitFormat(2, couponDiscount / 100), couponDiscount));
        if (yenCarPayPrice > 0) payDetails.add(new OrderVO.PayDetail(CARD_PAY_FEE, MoneyUtils.unitFormat(2, yenCarPayPrice / 100), yenCarPayPrice));

        order.setPayDetail(payDetails);
        order.setCourse(courseService.convert2CourseVO(course, true));

        Button button = new Button();
        button.setTitle("取消预约");


        order.setButton(button);

        return order;
    }

    @Override
    public OrderVO build(long courseId) {
        return render(courseId, null, null, 1, new Context());
    }

    @Override
    public OrderVO adjust(long courseId, Long cardId, Long couponId, int personTime) {
        return render(courseId, cardId, couponId, personTime, new Context());
    }

    @Override
    public int getBoughtNum(long userId) {
        Condition condition = new Condition(OrderMain.class);

        condition.createCriteria().andCondition("order_id=", userId)
                .andIn("status", Lists.newArrayList(OrderVO.Status.END.getCode()));


        return orderManager.selectCount(condition);
    }

    @Override
    public String create(long courseId, Long couponId, int personTime, Long cardId) {
        Context context = new Context();
        OrderVO orderVO = render(courseId, cardId, couponId, personTime, context);
        String orderId = MakeOrderNum.makeOrderNum();
        List<OrderVO.PayDetail> payDetails= orderVO.getPayDetail();
//
//        int realFee = 0, cardDiscountFee = 0, couponFee = 0, totalFee = 0, cardFee = 0;
//
//
//        for (OrderVO.PayDetail payDetail : payDetails) {
//            if (payDetail.getTitle().equals(TOTAL_FEE)) {
//                totalFee = payDetail.getOriginFee();
//                continue;
//            }
//
//            if (payDetail.getTitle().equals(COUPON_FEE)) {
//                couponFee = payDetail.getOriginFee();
//                continue;
//            }
//
//            if (payDetail.getTitle().equals(CARD_DISCOUNT)) {
//                cardDiscountFee = payDetail.getOriginFee();
//                continue;
//            }
//
//            if (payDetail.getTitle().equals(CARD_PAY_FEE)) {
//                cardFee = payDetail.getOriginFee();
//                continue;
//            }
//        }

        int realPay = orderVO.getRealPay().getOriginFee();
        Map<String, OrderVO.PayDetail> payDetailMap = context.getPayDetailMap();

        fundDetailService.incomeByPer(orderId, payDetailMap, realPay);

        // 生成订单
        OrderMain order = make(orderId, context.getUser().getId(), courseId, payDetailMap, realPay,
                cardId, couponId, OrderVO.Status.PENDING.getCode(), OrderVO.Type.COURSE.getCode());

        sava(order);

        // 礼券要锁定
        couponService.updateCouponUserStatus(couponId, CouponVO.Status.PENDING.getCode(),
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

        // 暂不用
//        order.setCard(cardService.convert2CardVO(card));

        // 3. 获取课程信息
        Course course = courseService.getNormalCourse().get(courseId);
        if (course == null) throw new BizException("没找到有效课程");
        order.setCourse(courseService.convert2CourseVO(course, true));

        context.setUser(user);
        context.setCard(card);
        context.setCourse(course);

        // 4. 找到礼券
        List<CouponVO> couponVOs = couponService.getCoupon4PayPerView(user.getId(),
                course.getPrice() * personTime, couponId, context);
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
        Map<String, OrderVO.PayDetail> payDetailMap = calFeeDetail(course.getPrice(), personTime, card,
                context.getCoupon(), null, payDetails, true);
        order.setPayDetail(payDetails);

        context.setPayDetailMap(payDetailMap);
        OrderVO.PayDetail realPay = calRealPay(payDetailMap);
        order.setRealPay(realPay);

        // 4. 按钮
        Button button = new Button();
        button.setTitle("支付（" + realPay.getFee() + "）");

        order.setButton(button);

        return order;
    }

    @Override
    public OrderVO.PayDetail calRealPay(Map<String, OrderVO.PayDetail> payDetailMap) {
        int total = fundDetailService.getFee(payDetailMap.get(TOTAL_FEE));
        int couponDiscount = fundDetailService.getFee(payDetailMap.get(COUPON_FEE));
        int cardDiscount = fundDetailService.getFee(payDetailMap.get(CARD_DISCOUNT));
        int cardPay = fundDetailService.getFee(payDetailMap.get(CARD_PAY_FEE));

        /* 实付款 */
        int realPayFee = total - couponDiscount - cardDiscount - cardPay;

        return new OrderVO.PayDetail(REAL_PAY_FEE, MoneyUtils.unitFormat(2, realPayFee / 100), realPayFee);
    }

    @Override
    public Map<String, OrderVO.PayDetail> calFeeDetail(int unitPrice, int num, YenCard card, CouponTemplate coupon, CouponTemplate rechargeTemplate,
                                                        List<OrderVO.PayDetail> payDetails, boolean isPer) {
        checkArgument(unitPrice > NumberUtils.INTEGER_ZERO);
        checkArgument(num > NumberUtils.INTEGER_ZERO);

        Map<String, OrderVO.PayDetail> map = Maps.newHashMap();
        OrderVO.PayDetail payDetail;

        /* 总价 单价 * 数量 */
        int total = unitPrice * num;
        payDetail = new OrderVO.PayDetail(TOTAL_FEE, MoneyUtils.unitFormat(2, total / 100), total);
        payDetails.add(payDetail);
        map.put(TOTAL_FEE, payDetail);
        int balance = total;

        /* 在线立减 */
        int reduce = onlineReduceFee;
        if (reduce > NumberUtils.INTEGER_ZERO) {
            if (reduce > balance) reduce = balance;

            payDetail = new OrderVO.PayDetail(ONLINE_REDUCE, MoneyUtils.minusUnitFormat(2, reduce / 100), reduce);
            payDetails.add(payDetail);
            map.put(ONLINE_REDUCE, payDetail);

            balance = balance - reduce;
            if (balance == NumberUtils.INTEGER_ZERO) return map;
        }

         /* 礼券 礼券能抵扣的价格 */
        int couponDiscount = 0;
        if (coupon != null) {
            couponDiscount = coupon.getPrice();
            if (coupon.getPrice() > balance) couponDiscount = balance;

            payDetail = new OrderVO.PayDetail(COUPON_FEE, MoneyUtils.minusUnitFormat(2, couponDiscount / 100), couponDiscount);
            payDetails.add(payDetail);
            map.put(COUPON_FEE, payDetail);

            balance = balance - couponDiscount;
            if (balance == NumberUtils.INTEGER_ZERO) return map;
        }

        /* 瘾卡优惠 */
        if (isPer && card != null && (card.getCashAccount() + card.getGiftAccount() >= balance * card.getDiscountRate() / 100)) {
            int cardDiscount = balance - balance * card.getDiscountRate() / 100;
            payDetail = new OrderVO.PayDetail(CARD_DISCOUNT, MoneyUtils.minusUnitFormat(2, cardDiscount), cardDiscount);
            payDetails.add(payDetail);
            map.put(CARD_DISCOUNT, payDetail);

            balance = balance - couponDiscount;
            if (balance == NumberUtils.INTEGER_ZERO) return map;
        }

        /* 瘾卡支付 */
        if (isPer && card != null) {
            int cardPay;
            if (card.getCashAccount() + card.getGiftAccount() > balance) {
                cardPay = balance;
                payDetail = new OrderVO.PayDetail(CARD_PAY_FEE, MoneyUtils.minusUnitFormat(2, cardPay), cardPay);
                payDetails.add(payDetail);
                map.put(CARD_PAY_FEE, payDetail);
            } else if (card.getCashAccount() + card.getGiftAccount() > NumberUtils.INTEGER_ZERO) {
                cardPay = card.getCashAccount() + card.getGiftAccount();
                payDetail = new OrderVO.PayDetail(CARD_PAY_FEE, MoneyUtils.minusUnitFormat(2, cardPay), cardPay);
                payDetails.add(payDetail);
                map.put("cardPay", payDetail);
            }
        }

         /* 充值赠送 */
        if (!isPer && rechargeTemplate != null) {
            int gift = rechargeTemplate.getPrice();
            payDetail = new OrderVO.PayDetail(GIFT_FEE, MoneyUtils.minusUnitFormat(2, gift), gift);
            payDetails.add(payDetail);
            map.put(GIFT_FEE, payDetail);

            balance = balance - couponDiscount;
            if (balance == NumberUtils.INTEGER_ZERO) return map;
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
    public OrderMain make(String orderId, Long userId, Long classId, Map<String, OrderVO.PayDetail> payDetailMap, int realPay,
                             Long yenCardId, Long couponId, Byte status, Byte type) {
        int cardDiscountFee = payDetailMap.get(CARD_DISCOUNT) != null ? payDetailMap.get(CARD_DISCOUNT).getOriginFee() : NumberUtils.INTEGER_ZERO;
        int couponFee = payDetailMap.get(COUPON_FEE) != null ? payDetailMap.get(COUPON_FEE).getOriginFee() : NumberUtils.INTEGER_ZERO;
        int totalFee = payDetailMap.get(TOTAL_FEE) != null ? payDetailMap.get(TOTAL_FEE).getOriginFee() : NumberUtils.INTEGER_ZERO;
        int cardFee = payDetailMap.get(CARD_PAY_FEE) != null ? payDetailMap.get(CARD_PAY_FEE).getOriginFee() : NumberUtils.INTEGER_ZERO;
        int onlineDiscountFee = payDetailMap.get(ONLINE_REDUCE) != null ? payDetailMap.get(ONLINE_REDUCE).getOriginFee() : NumberUtils.INTEGER_ZERO;
        int giftFee = payDetailMap.get(GIFT_FEE) != null ? payDetailMap.get(GIFT_FEE).getOriginFee() : NumberUtils.INTEGER_ZERO;

        OrderMain order = new OrderMain();

        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setClassId(classId);
        order.setRealPay(realPay);
        order.setTotalPrice(totalFee);
        order.setYenCarPayPrice(cardFee);
        order.setYenCarDiscount(cardDiscountFee);
        order.setYenCarId(yenCardId);
        order.setOnlineDiscount(onlineDiscountFee);
        order.setOnlineRule("立减" + onlineDiscountFee + "分");
        order.setCouponDiscount(couponFee);
        order.setCouponId(couponId);
        order.setGiftDiscount(giftFee);
        order.setStatus(status);
        order.setType(type);

        return order;
    }
}
