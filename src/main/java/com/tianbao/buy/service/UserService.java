package com.tianbao.buy.service;

import com.tianbao.buy.domain.User;

public interface UserService {
    User getUserByWxUnionId();

    User getUserByuserId(long userId);

    void updatePhone(long userId, String phone);
}
