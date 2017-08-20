package com.tianbao.buy.service;

import com.tianbao.buy.vo.FundDetailVO;

import java.util.Date;

public interface FundDetailService {
    void updateStatus(String orderId, FundDetailVO.Status status);

    void initFund4PerIn(String orderId, Integer price4wx, Integer price4Card, Integer price4Coupon, Date date);

    void initFund4PerOut(String orderId, Integer price4wx, Integer price4Card, Integer price4Coupon, Date date);

    void initFund4RechargIn(String orderId, Integer price4wx, Integer price4Gift, Integer price4Coupon, Date date);
}
