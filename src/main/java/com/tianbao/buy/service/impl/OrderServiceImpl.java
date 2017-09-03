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
import org.springframework.transaction.annotation.Transactional;
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
    public int onlineReduceFee;

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
    public void updateStatus(OrderMain order, OrderVO.Status originStatus, String orderId) {
        Condition condition = new Condition(OrderMain.class);
        condition.createCriteria().andEqualTo("status", originStatus.getCode()).andCondition("order_id=", orderId);

        orderManager.update(order, condition);
    }

    @Override
    public void cancel(String orderId) {
        updateOrder(orderId, OrderVO.Status.ORDER, null);
        List<FundDetail> fundDetails = fundDetailService.get(orderId, FundDetailVO.Status.FINISH);
        int realPay = fundDetailService.getRealPayFee(fundDetails);

        if (realPay == NumberUtils.INTEGER_ZERO) {
            // todo 直接调用卡退钱，否则调微信退，微信退完才能继续完结
        }


    }

    @Override
    public List<OrderVO> get(byte status) {
        User user = userService.getUserByWxUnionId();

        Condition condition = new Condition(OrderMain.class);
        condition.orderBy("createTime");
        condition.createCriteria().andEqualTo("status", status).andCondition("user_id=", user.getId())
                .andCondition("type=", OrderVO.Type.COURSE.getCode());

        List<OrderMain> orders = orderManager.findByCondition(condition);

        Set<Long> courseIds = Sets.newHashSet();

        for (OrderMain orderMain : orders) {
            courseIds.add(orderMain.getClassId());
        }

        Map<Long, Course> courseMap = courseService.getCourse(courseIds);

        List<OrderVO> orderVOs = Lists.newArrayList();

        for (OrderMain orderMain : orders) {
            OrderVO orderVO = convert2OrderVO(orderMain, courseMap.get(orderMain.getClassId()), false);
            orderVOs.add(orderVO);
        }

        return orderVOs;
    }

    private OrderVO convert2OrderVO(OrderMain orderMain, Course course, boolean isDetail) {
        checkNotNull(orderMain);
        checkNotNull(course);

        OrderVO order = new OrderVO();
        List<FundDetail> fundDetails = fundDetailService.get(orderMain.getOrderId(), null);

        if (isDetail) {
            order.setPayDetail(cardService.getPayDetail(fundDetails));

            Button button = new Button();
            button.setTitle("取消预约");
            order.setButton(button);
        }

        order.setCourse(courseService.convert2CourseVO(course, true));
        order.setRealPay(cardService.getRealPay(fundDetails));

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

        condition.createCriteria().andCondition("user_id=", userId).andCondition("type=", OrderVO.Type.COURSE.getCode())
                .andIn("status", Lists.newArrayList(OrderVO.Status.END.getCode()));


        return orderManager.selectCount(condition);
    }

    @Override
    @Transactional
    public String create(long courseId, Long couponUserId, Byte personTime) {
        checkArgument(personTime > NumberUtils.INTEGER_ZERO);

        // 1. 找到用户的瘾卡
        User user = userService.getUserByWxUnionId();
        YenCard card = cardService.getDefault(user.getId()); // 主要是判断下卡是否存在

        CouponTemplate couponTemplate = null;

        // 2. 获取课程信息
        Course course = courseService.getNormalCourse().get(courseId);
        if (course == null) throw new BizException("没找到有效课程");

        // 3. 找礼券
        if (couponUserId != null && couponUserId > NumberUtils.LONG_ZERO) {
            CouponUser couponUser = couponService.getCouponUser(couponUserId);

            if (!couponUser.getStatus().equals(CouponVO.Status.NORMAL.getCode())) {
                logger.error(String.format("礼券状态无效。id=%d", couponUserId));
                throw new BizException("礼券无效");
            }

            // 4. 礼券用的模版
            couponTemplate = couponService.getTemplate(couponUser.getCouponTemplateId());

            if (couponTemplate == null || couponTemplate.getRulePrice() > personTime * course.getPrice()) {
                logger.error(String.format("礼券额度不匹配无效。id=%d", couponUserId));
                throw new BizException(String.format("礼券无效", couponUserId));
            }
        }

        String orderId = MakeOrderNum.makeOrderNum();

        List<OrderVO.PayDetail> payDetails = Lists.newArrayList();
        Map<String, OrderVO.PayDetail> payDetailMap = calFeeDetail(course.getPrice(), personTime, card,
                couponTemplate, null, payDetails, true);

        int realPay = calRealPay(payDetailMap).getOriginFee();

        fundDetailService.incomeByPer(orderId, payDetailMap, realPay);

        // 生成订单
        OrderMain order = make(orderId, personTime, user.getId(), courseId, card.getId(), couponUserId,
                OrderVO.Status.PENDING_PAY.getCode(), OrderVO.Type.COURSE.getCode(), null);

        sava(order);

        // 锁定礼券
        if (couponUserId != null && couponUserId > NumberUtils.LONG_ZERO) {
            couponService.updateCouponUserStatus(couponUserId, CouponVO.Status.PENDING.getCode(),
                    CouponVO.Status.NORMAL.getCode());
        }

        return orderId;
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

        context.setPayDetailMap(payDetailMap);
        OrderVO.PayDetail realPay = calRealPay(payDetailMap);
        order.setRealPay(realPay);
        order.setPayDetail(merge(payDetails));

        // 4. 按钮
        Button button = new Button();
        button.setTitle("支付（" + realPay.getFee() + "）");

        order.setButton(button);

        return order;
    }

    private List<OrderVO.PayDetail> merge(List<OrderVO.PayDetail> origin) {
        int cash = 0, gift = 0;
        List<OrderVO.PayDetail> payDetails = Lists.newArrayList();

        for (OrderVO.PayDetail payDetail : origin) {
            if (CARD_CASH_PAY_FEE.equals(payDetail.getTitle())) {
                cash = payDetail.getOriginFee();
                continue;
            }

            if (CARD_GIFT_PAY_FEE.equals(payDetail.getTitle())) {
                gift = payDetail.getOriginFee();
                continue;
            }

            payDetails.add(payDetail);
        }

        if (cash + gift > NumberUtils.INTEGER_ZERO) {
            OrderVO.PayDetail payDetail = new OrderVO.PayDetail(CARD_PAY_FEE, MoneyUtils.minusUnitFormat(2,
                    (cash + gift) / 100d), cash + gift);
            payDetails.add(payDetail);
        }

        return payDetails;
    }

    @Override
    public OrderVO.PayDetail calRealPay(Map<String, OrderVO.PayDetail> payDetailMap) {
        int total = fundDetailService.getFee(payDetailMap.get(TOTAL_FEE));
        int couponDiscount = fundDetailService.getFee(payDetailMap.get(COUPON_FEE));
        int cardDiscount = fundDetailService.getFee(payDetailMap.get(CARD_DISCOUNT));
        int cardGiftPay = fundDetailService.getFee(payDetailMap.get(CARD_GIFT_PAY_FEE));
        int cardCashPay = fundDetailService.getFee(payDetailMap.get(CARD_CASH_PAY_FEE));
        int onlineReduce = fundDetailService.getFee(payDetailMap.get(ONLINE_REDUCE));

        /* 实付款 */
        int realPayFee = total - couponDiscount - cardDiscount - cardGiftPay - cardCashPay - onlineReduce;
        if (realPayFee < NumberUtils.INTEGER_ZERO) realPayFee = NumberUtils.INTEGER_ZERO;

        return new OrderVO.PayDetail(REAL_PAY_FEE, MoneyUtils.unitFormat(2, realPayFee / 100d), realPayFee);
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
        payDetail = new OrderVO.PayDetail(TOTAL_FEE, MoneyUtils.unitFormat(2, total / 100d), total);
        payDetails.add(payDetail);
        map.put(TOTAL_FEE, payDetail);
        int balance = total;

        /* 在线立减 */
        int reduce = onlineReduceFee;
        if (reduce > NumberUtils.INTEGER_ZERO && isPer) {
            if (reduce > balance) reduce = balance;

            payDetail = new OrderVO.PayDetail(ONLINE_REDUCE, MoneyUtils.minusUnitFormat(2, reduce / 100d), reduce);
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

            payDetail = new OrderVO.PayDetail(COUPON_FEE, MoneyUtils.minusUnitFormat(2, couponDiscount / 100d), couponDiscount);
            payDetails.add(payDetail);
            map.put(COUPON_FEE, payDetail);

            balance = balance - couponDiscount;
            if (balance == NumberUtils.INTEGER_ZERO) return map;
        }

        /* 瘾卡优惠 */
        if (isPer && card != null && (card.getCashAccount() + card.getGiftAccount() >= balance * card.getDiscountRate() / 100)) {
            int cardDiscount = balance - balance * card.getDiscountRate() / 100;
            payDetail = new OrderVO.PayDetail(CARD_DISCOUNT, MoneyUtils.minusUnitFormat(2, cardDiscount / 100d), cardDiscount);
            payDetails.add(payDetail);
            map.put(CARD_DISCOUNT, payDetail);

            balance = balance - cardDiscount;
            if (balance == NumberUtils.INTEGER_ZERO) return map;
        }

        /* 瘾卡支付 */
        if (isPer && card != null) {
            int cash = card.getCashAccount();
            int gift = card.getGiftAccount();
            int cashPay = 0, giftPay = 0;

            if (gift > NumberUtils.INTEGER_ZERO) {
                if (balance <= gift) {
                    giftPay = balance;
                } else {
                    giftPay = gift;
                }
                balance = balance - giftPay;
            }

            if (cash > NumberUtils.INTEGER_ZERO && balance > NumberUtils.INTEGER_ZERO) {
                if (cash >= balance) {
                    cashPay = balance;
                } else {
                    cashPay = cash;
                }

            }

            if (cashPay > NumberUtils.INTEGER_ZERO) {
                payDetail = new OrderVO.PayDetail(CARD_CASH_PAY_FEE, MoneyUtils.minusUnitFormat(2, cashPay / 100d), cashPay);
                payDetails.add(payDetail);
                map.put(CARD_CASH_PAY_FEE, payDetail);
            }

            if (giftPay > NumberUtils.INTEGER_ZERO) {
                payDetail = new OrderVO.PayDetail(CARD_GIFT_PAY_FEE, MoneyUtils.minusUnitFormat(2, giftPay / 100d), giftPay);
                payDetails.add(payDetail);
                map.put(CARD_GIFT_PAY_FEE, payDetail);
            }
        }

         /* 充值赠送 */
        if (!isPer && rechargeTemplate != null) {
            int gift = rechargeTemplate.getPrice();
            payDetail = new OrderVO.PayDetail(GIFT_FEE, MoneyUtils.minusUnitFormat(2, gift / 100), gift);
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
    public OrderMain getOrder(String orderId, OrderVO.Status originStatus) {
        // 先找原始订单
        Condition condition = new Condition(OrderMain.class);
        condition.createCriteria().andEqualTo("status", originStatus.getCode()).andCondition("order_id=", orderId);

        List<OrderMain> orderMains = orderManager.findByCondition(condition);

        if (orderMains == null || orderMains.size() != NumberUtils.INTEGER_ONE)
            throw new BizException(String.format("没找到状态为[%d]orderId[%s]的订单", originStatus.getCode(), orderId));
        return orderMains.get(NumberUtils.INTEGER_ZERO);
    }

    @Override
    public OrderMain make(String orderId, Byte personTime, Long userId, Long classId, Long yenCardId,
                          Long couponId, Byte status, Byte type, Long rechargeTemplateId) {
        OrderMain order = new OrderMain();

        order.setOrderId(orderId);
        order.setPersonTime(personTime);
        order.setUserId(userId);
        order.setClassId(classId);
        order.setYenCardId(yenCardId);
        order.setCouponId(couponId);
        order.setStatus(status);
        order.setType(type);
        order.setRechargeTemplateId(rechargeTemplateId);

        return order;
    }

    @Override
    public OrderMain updateOrder(String orderId, OrderVO.Status originStatus, String payOrderId) {
        // 先找原始订单
        OrderMain origin = getOrder(orderId, originStatus);
        OrderMain order = new OrderMain();

        if (origin.getType().equals(OrderVO.Type.CARD.getCode())) {
            order.setStatus(OrderVO.Status.END.getCode());
        } else {
            if (originStatus.equals(OrderVO.Status.PENDING_PAY)) {
                order.setStatus(OrderVO.Status.ORDER.getCode());
            }

            if (originStatus.equals(OrderVO.Status.ORDER)) {
                order.setStatus(OrderVO.Status.PENDING_CANCLE.getCode());
            }

            if (originStatus.equals(OrderVO.Status.PENDING_CANCLE)) {
                order.setStatus(OrderVO.Status.CANCLED.getCode());
            }
        }

        if (originStatus.equals(OrderVO.Status.PENDING_PAY)) {
            order.setPayTime(new Date());
            order.setPayOrderId(payOrderId);
        }

        updateStatus(order, originStatus, orderId);
        return origin;
    }
}
