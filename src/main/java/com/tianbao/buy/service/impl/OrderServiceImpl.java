package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.OrderMapper;
import com.tianbao.buy.model.Order;
import com.tianbao.buy.service.OrderService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class OrderServiceImpl extends AbstractService<Order> implements OrderService {
    @Resource
    private OrderMapper orderMapper;

}
