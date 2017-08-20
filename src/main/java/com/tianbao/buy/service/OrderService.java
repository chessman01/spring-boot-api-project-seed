package com.tianbao.buy.service;

import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.vo.OrderVO;

public interface OrderService {
    OrderVO build(long courseId);

    OrderVO adjust(long courseId, long couponId, int personTime);

    OrderMain convert(String orderId, Long userId, Long classId, Integer realPay, Integer totalPrice,
                  Integer yenCarPayPrice, Integer yenCarDiscount, Long yenCarId, Integer onlineDiscount,
                  String onlineRule, Integer couponDiscount, Long couponId, Integer giftDiscount, Byte status);

    void sava (OrderMain order);

//    long create(long courseId, long cardId, long couponId, int personTime);


}
