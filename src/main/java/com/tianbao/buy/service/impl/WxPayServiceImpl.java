package com.tianbao.buy.service.impl;

import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.manager.OrderMainManager;
import com.tianbao.buy.service.*;
import com.tianbao.buy.vo.CouponVO;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class WxPayServiceImpl implements WxPayService {
    @Resource
    private CouponService couponService;

    @Resource
    private YenCardService yenCardService;

    @Resource
    private OrderMainManager orderMainManager;

    @Resource
    private FundDetailService fundDetailService;

    @Resource
    private OrderService orderService;

    @Override
    @Transactional
    public void cancel(String orderId) {
        OrderMain orderMain = this.updateOrder(orderId, OrderVO.Status.CANCLED);
        this.updateFund(orderId, FundDetailVO.Status.FINISH, FundDetailVO.Status.CANCELED);

        if (orderMain.getCouponId() != null && orderMain.getCouponId() > NumberUtils.LONG_ZERO) {
            couponService.updateCouponUserStatus(orderMain.getCouponId(), CouponVO.Status.PENDING.getCode(),
                    CouponVO.Status.USED.getCode());
        }
    }

    @Override
    @Transactional
    public void paySuccess(String orderId) {
        OrderMain orderMain = this.updateOrder(orderId, OrderVO.Status.PENDING_PAY);
        this.updateFund(orderId, FundDetailVO.Status.PENDING, FundDetailVO.Status.FINISH);

        if (orderMain.getCouponId() != null && orderMain.getCouponId() > NumberUtils.LONG_ZERO) {
            couponService.updateCouponUserStatus(orderMain.getCouponId(), CouponVO.Status.USED.getCode(),
                    CouponVO.Status.PENDING.getCode());
        }
    }

    private OrderMain updateOrder(String orderId, OrderVO.Status originStatus) {
        // 先找原始订单
        OrderMain origin = orderService.getOrder(orderId, originStatus);
        OrderMain order = new OrderMain();

        if (origin.getType().equals(OrderVO.Type.CARD.getCode())) {
            order.setStatus(OrderVO.Status.END.getCode());
        } else {
            if (originStatus.equals(OrderVO.Status.PENDING_PAY)) {
                order.setStatus(OrderVO.Status.ORDER.getCode());
            }

            if (originStatus.equals(OrderVO.Status.PENDING_CANCLE)) {
                order.setStatus(OrderVO.Status.CANCLED.getCode());
            }
        }

        orderService.updateStatus(order, originStatus, orderId);
        return origin;
    }

    private List<FundDetail> updateFund(String orderId, FundDetailVO.Status originStatus, FundDetailVO.Status status) {
        List<FundDetail> fundDetails = fundDetailService.get(orderId, originStatus);

        if (CollectionUtils.isEmpty(fundDetails)) throw new BizException(String.format("没找到orderId[%s]的资金流水", orderId));

        fundDetailService.updateStatus(orderId, originStatus, status);

        return fundDetails;
    }
}
