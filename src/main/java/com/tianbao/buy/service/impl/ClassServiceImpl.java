package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.ClassMapper;
import com.tianbao.buy.model.Class;
import com.tianbao.buy.service.ClassService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class ClassServiceImpl extends AbstractService<Class> implements ClassService {
    @Resource
    private ClassMapper classMapper;

}
