package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.FundDetailDAO;
import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/16.
 */
@Service
public class FundDetailManagerImpl extends AbstractManager<FundDetail> implements FundDetailManager {
    @Resource
    private FundDetailDAO fundDetailDAO;

}
