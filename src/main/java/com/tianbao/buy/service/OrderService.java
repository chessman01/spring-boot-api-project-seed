package com.tianbao.buy.service;

import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderVO> get(byte status);

    int getBoughtNum(long userId);

    OrderVO build(long courseId);

    OrderVO adjust(long courseId, Long cardId, Long couponId, int personTime);

    String create(long courseId, Long couponId, int personTime, Long cardId);

    OrderMain make(String orderId, Long userId, Long classId, Map<String, OrderVO.PayDetail> payDetailMap, int realPay,
                   Long yenCardId, Long couponId, Byte status, Byte type);

    void sava (OrderMain order);

    public final static String TOTAL_FEE = "课程总价";

    public final static String COUPON_FEE = "礼券";

    public final static String CARD_DISCOUNT = "瘾卡优惠";

    public final static String CARD_PAY_FEE = "瘾卡支付";

    public final static String ONLINE_DISCOUNT = "在线支付立减";

    public final static String GIFT_FEE = "赠送";

    public final static String REAL_PAY_FEE = "实付款";
}
