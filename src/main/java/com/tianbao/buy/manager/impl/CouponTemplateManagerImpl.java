package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.CouponTemplateDAO;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.manager.CouponTemplateManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/20.
 */
@Service
public class CouponTemplateManagerImpl extends AbstractManager<CouponTemplate> implements CouponTemplateManager {
    @Resource
    private CouponTemplateDAO couponTemplateDAO;

}
