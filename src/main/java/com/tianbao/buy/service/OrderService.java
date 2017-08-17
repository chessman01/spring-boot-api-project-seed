package com.tianbao.buy.service;

import com.tianbao.buy.vo.OrderVO;

public interface OrderService {
    OrderVO build(long courseId);

    OrderVO adjust(long courseId, long couponId, int personTime);

    long create(long courseId, long cardId, long couponId, int personTime);

}
