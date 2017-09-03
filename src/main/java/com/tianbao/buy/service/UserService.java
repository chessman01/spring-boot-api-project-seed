package com.tianbao.buy.service;

import com.tianbao.buy.domain.User;
import com.tianbao.buy.vo.InvitationVO;
import com.tianbao.buy.vo.UserVO;

public interface UserService {
    UserVO self();

    User getUserByWxUnionId();

    User getUserByuserId(long userId);

    void updatePhone(long userId, String phone);

    InvitationVO invitation();

    boolean getPin(String phone, boolean isObtainRecommend);

    boolean validatePhone(String code, String phone);

    void recommend(long inviterId, String code, String phone);
}
