package com.tianbao.buy.service.impl;

import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.domain.Order;
import com.tianbao.buy.manager.OrderManager;
import com.tianbao.buy.service.OrderService;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService{
    @Resource
    private OrderManager orderManager;

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

//    @Override
//    public long create(long orderId, long userId, Integer classId, long courseId, long cardId, long couponId, int personTime) {
////        convert(orderId, userId, classId, Integer realPay, Integer totalPrice,
////                Integer yenCarPayPrice, Integer yenCarDiscount, Long yenCarId, Integer onlineDiscount,
////                String onlineRule, Integer couponDiscount, Long couponId)
//
//
//
//
//        return 0;
//    }

    private void updateStatus(Long orderId, OrderVO.Status status, OrderVO.Status oldStatus,
                              Date payTime, String payOrderId) {
        Condition condition = new Condition(Order.class);

        condition.createCriteria().andCondition("order_id=", orderId)
                .andCondition("status=", oldStatus.getCode());

        Order order = new Order();
        order.setStatus(status.getCode());
        order.setPayTime(payTime);
        order.setPayOrderId(payOrderId);

        orderManager.update(order, condition);
    }

    public Order convert(Long orderId, Long userId, Long classId, Integer realPay, Integer totalPrice,
                          Integer yenCarPayPrice, Integer yenCarDiscount, Long yenCarId, Integer onlineDiscount,
                          String onlineRule, Integer couponDiscount, Long couponId) {
        Order order = new Order();

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

        return order;
    }
}
