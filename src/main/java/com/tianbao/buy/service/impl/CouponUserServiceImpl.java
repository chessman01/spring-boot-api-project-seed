package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.CouponUserMapper;
import com.tianbao.buy.model.CouponUser;
import com.tianbao.buy.service.CouponUserService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class CouponUserServiceImpl extends AbstractService<CouponUser> implements CouponUserService {
    @Resource
    private CouponUserMapper couponUserMapper;

}
