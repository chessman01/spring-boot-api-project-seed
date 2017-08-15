package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.UserMapper;
import com.tianbao.buy.model.User;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
