package com.tianbao.buy.service;

import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FundDetailService {
    void updateStatus(String orderId, FundDetailVO.Status status);

    void initFund4PerIn(String orderId, Integer price4wx, Map<String, OrderVO.PayDetail> payDetailMap, Date date);

    void initFund4PerOut(String orderId, Integer price4wx, Integer price4Card, Integer price4Coupon, Date date);

    void initFund4RechargIn(String orderId, Integer price4wx, Map<String, OrderVO.PayDetail> payDetailMap, Date date);
}
