package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.FundDetailMapper;
import com.tianbao.buy.model.FundDetail;
import com.tianbao.buy.service.FundDetailService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class FundDetailServiceImpl extends AbstractService<FundDetail> implements FundDetailService {
    @Resource
    private FundDetailMapper fundDetailMapper;

}
