package com.tianbao.buy.service;

import com.tianbao.buy.domain.Order;
import com.tianbao.buy.vo.OrderVO;

public interface OrderService {
    OrderVO build(long courseId);

    OrderVO adjust(long courseId, long couponId, int personTime);

    Order convert(String orderId, Long userId, Long classId, Integer realPay, Integer totalPrice,
                  Integer yenCarPayPrice, Integer yenCarDiscount, Long yenCarId, Integer onlineDiscount,
                  String onlineRule, Integer couponDiscount, Long couponId, Integer giftDiscount, OrderVO.Status status);

    void sava (Order order);

//    long create(long courseId, long cardId, long couponId, int personTime);


}
