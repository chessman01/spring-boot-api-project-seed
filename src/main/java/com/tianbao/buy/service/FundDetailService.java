package com.tianbao.buy.service;

import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface FundDetailService {
    void updateStatus(String orderId, FundDetailVO.Status status);

    List<FundDetail> refundByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    List<FundDetail> incomeByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    List<FundDetail> refundByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    List<FundDetail> incomeByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    int getFee(OrderVO.PayDetail payDetail);
}
