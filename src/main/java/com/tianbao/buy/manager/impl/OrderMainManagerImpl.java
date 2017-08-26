package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.OrderMainDAO;
import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.manager.OrderMainManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/26.
 */
@Service
public class OrderMainManagerImpl extends AbstractManager<OrderMain> implements OrderMainManager {
    @Resource
    private OrderMainDAO orderMainDAO;

}
