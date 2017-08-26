package com.tianbao.buy.service;

import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.vo.OrderVO;

public interface OrderService {
    int getBoughtNum(long userId);

    OrderVO build(long courseId);

    OrderVO adjust(long courseId, Long cardId, Long couponId, int personTime);

    String create(long courseId, long couponId, int personTime, long cardId);

    OrderMain convert(String orderId, Long userId, Long classId, Integer realPay, Integer totalPrice,
                  Integer yenCarPayPrice, Integer yenCarDiscount, Long yenCarId, Integer onlineDiscount,
                  String onlineRule, Integer couponDiscount, Long couponId, Integer giftDiscount, Byte status);

    void sava (OrderMain order);

//    long create(long courseId, long cardId, long couponId, int personTime);

    public final static String TOTAL_FEE = "课程总价";

    public final static String COUPON_FEE = "礼券";

    public final static String CARD_DISCOUNT = "瘾卡优惠";

    public final static String CARD_PAY_FEE = "瘾卡支付";

    public final static String REAL_PAY_FEE = "实付款";
}
