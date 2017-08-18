package com.tianbao.buy.service;

import com.tianbao.buy.vo.FundDetailVO;

public interface FundDetailService {
    void updateStatus(Long orderId, FundDetailVO.Status status);

    void initFund4PerIn(Long orderId, Integer price4wx, Integer price4Card, Integer price4Coupon);

    void initFund4PerOut(Long orderId, Integer price4wx, Integer price4Card, Integer price4Coupon);

    void initFund4RechargIn(Long orderId, Integer price4wx, Integer price4Gift, Integer price4Coupon);
}
