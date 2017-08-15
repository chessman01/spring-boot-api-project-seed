package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.CouponTemplateMapper;
import com.tianbao.buy.model.CouponTemplate;
import com.tianbao.buy.service.CouponTemplateService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class CouponTemplateServiceImpl extends AbstractService<CouponTemplate> implements CouponTemplateService {
    @Resource
    private CouponTemplateMapper couponTemplateMapper;

}
