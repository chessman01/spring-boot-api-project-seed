package com.tianbao.buy.service;

import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface FundDetailService {
    List<FundDetail> get(String orderId, FundDetailVO.Status originStatus);

    void updateStatus(String orderId, FundDetailVO.Status originStatus, FundDetailVO.Status status);

    void refundByPer(String orderId);

    List<FundDetail> incomeByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    List<FundDetail> incomeByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    int getFee(OrderVO.PayDetail payDetail);
}
