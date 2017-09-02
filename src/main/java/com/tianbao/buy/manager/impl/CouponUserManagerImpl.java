package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.CouponUserDAO;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.manager.CouponUserManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/09/02.
 */
@Service
public class CouponUserManagerImpl extends AbstractManager<CouponUser> implements CouponUserManager {
    @Resource
    private CouponUserDAO couponUserDAO;

}
