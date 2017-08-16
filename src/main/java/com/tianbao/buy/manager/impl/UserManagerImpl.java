package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.UserDAO;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.manager.UserManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/16.
 */
@Service
public class UserManagerImpl extends AbstractManager<User> implements UserManager {
    @Resource
    private UserDAO userDAO;

}
