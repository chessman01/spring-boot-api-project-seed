package com.tianbao.buy.service;

import com.tianbao.buy.domain.User;

public interface WxPayService {
    void paySuccess(String orderId, User user);

    void cancel(String orderId, User user);

}
