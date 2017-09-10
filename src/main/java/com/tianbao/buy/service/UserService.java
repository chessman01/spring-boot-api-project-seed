package com.tianbao.buy.service;

import com.aliyuncs.exceptions.ClientException;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.vo.InvitationVO;
import com.tianbao.buy.vo.UserVO;
import me.chanjar.weixin.common.exception.WxErrorException;

public interface UserService {
    UserVO self(User user);

    User getUserByWxOpenId(String openId, String lang) throws WxErrorException;

    User getUserByuserId(long userId);

    void updatePhone(long userId, String phone);

    InvitationVO invitation(User user);

    String getPin(String phone, boolean isObtainRecommend, User user) throws ClientException;

    String validatePhone(String code, String phone, User user);

    String recommend(long inviterId, String code, String phone, User user);
}
