package com.tianbao.buy.service;

import static com.google.common.base.Preconditions.*;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.manager.UserManager;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

public abstract class BaseService {
    @Resource
    protected UserManager userManager;

    protected User getUserByWxUnionId() {
        // todo 这里是要依据微信接口拿到用户uid，到userManager查用户，然后得到用户ID
        String wxUnionId = "12345";

        if (StringUtils.isBlank(wxUnionId)) {
            // todo log
            throw new BizException("系统错误");
        }

        User user = userManager.findBy("wxUnionId", wxUnionId);

        if (user == null) {
            user = new User();

        }

        // 新建一个用户
        if (user == null) throw new BizException("用户没发现");

        return user;
    }

    protected User save() {
        User user = new User();

//        user.setWxOpenId();
//        user.setWxUnionId();
//        user.setNick();
//        user.setSex();
//
//        userManager.save()
        return null;
    }
}
