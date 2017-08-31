package com.tianbao.buy.service;

public interface WxPayService {
    void paySuccess(String orderId);

    void cancel(String orderId);

}
