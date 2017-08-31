package com.tianbao.buy.service;

import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;

import java.util.Map;

public interface FundDetailService {
    void updateStatus(String orderId, FundDetailVO.Status status);

    void refundByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    void incomeByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    void refundByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    void incomeByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    int getFee(OrderVO.PayDetail payDetail);
}
