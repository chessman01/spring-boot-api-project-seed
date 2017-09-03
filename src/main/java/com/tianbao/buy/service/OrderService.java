package com.tianbao.buy.service;

import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderVO detail(String orderId);

    void updateStatus(OrderMain order, OrderVO.Status originStatus, String orderId);

    OrderMain getOrder(String orderId, OrderVO.Status originStatus);

    void cancel(String orderId);

    List<OrderVO> get(byte status);

    int getBoughtNum(long userId);

    OrderVO build(long courseId);

    OrderVO adjust(long courseId, Long cardId, Long couponId, int personTime);

    String create(long courseId, Long couponId, Byte personTime);

    void sava (OrderMain order);

    OrderVO.PayDetail calRealPay(Map<String, OrderVO.PayDetail> payDetailMap);

    Map<String, OrderVO.PayDetail> calFeeDetail(int unitPrice, int num, YenCard card, CouponTemplate coupon, CouponTemplate rechargeTemplate,
                                                       List<OrderVO.PayDetail> payDetails, boolean isPer);

    OrderMain make(String orderId, Byte personTime, Long userId, Long classId, Long yenCardId,
                   Long couponId, Byte status, Byte type, Long rechargeTemplateId);

    OrderMain updateOrder(String orderId, OrderVO.Status originStatus, OrderVO.Status targetStatus, String payOrderId);

    public final static String TOTAL_FEE = "课程总价";

    public final static String COUPON_FEE = "礼券";

    public final static String CARD_DISCOUNT = "瘾卡优惠";

    public final static String CARD_CASH_PAY_FEE = "瘾卡现金账号支付";

    public final static String CARD_GIFT_PAY_FEE = "瘾卡赠送账号支付";

    public final static String CARD_PAY_FEE = "瘾卡支付";

    public final static String ONLINE_REDUCE = "在线支付立减";

    public final static String GIFT_FEE = "赠送";

    public final static String REAL_PAY_FEE = "实付款";
}
