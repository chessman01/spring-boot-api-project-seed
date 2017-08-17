package com.tianbao.buy.service.impl;

import com.tianbao.buy.service.OrderService;
import com.tianbao.buy.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService{
    @Override
    public OrderVO build(long courseId) {
        // 1. 课程信息
        // 2. 人次信息
        // 3. 优惠券信息
        // 4. 支付明细
        // 5. 实付金额
        // 6. 下单按钮
        return null;
    }

    @Override
    public OrderVO adjust(long courseId, long couponId, int personTime) {
        return null;
    }

    @Override
    public long create(long courseId, long cardId, long couponId, int personTime) {
        return 0;
    }
}
