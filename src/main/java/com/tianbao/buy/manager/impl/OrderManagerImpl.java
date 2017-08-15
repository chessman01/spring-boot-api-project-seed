package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.OrderDAO;
import com.tianbao.buy.domain.Order;
import com.tianbao.buy.manager.OrderManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class OrderManagerImpl extends AbstractManager<Order> implements OrderManager {
    @Resource
    private OrderDAO orderDAO;

}
