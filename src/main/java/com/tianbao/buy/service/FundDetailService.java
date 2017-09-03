package com.tianbao.buy.service;

import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface FundDetailService {
    List<FundDetail> get(String orderId, FundDetailVO.Status originStatus);

    void updateStatus(String orderId, FundDetailVO.Status originStatus, FundDetailVO.Status status);

    List<FundDetail> refundByPer(String orderId);

    List<FundDetail> incomeByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    List<FundDetail> incomeByRecharge(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx);

    int getCardFee(List<FundDetail> details, boolean isCash, boolean isRecharge);

    int getRealPayFee(List<FundDetail> details);

    int getFee(OrderVO.PayDetail payDetail);
}
