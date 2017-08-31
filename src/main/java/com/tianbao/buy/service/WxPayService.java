package com.tianbao.buy.service;

import com.tianbao.buy.vo.FundDetailVO;

public interface WxPayService {
    void paySuccess(String orderId, FundDetailVO.Direction direction);

}
