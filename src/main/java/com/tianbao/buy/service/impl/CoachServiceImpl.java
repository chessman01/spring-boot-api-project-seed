package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.CoachMapper;
import com.tianbao.buy.model.Coach;
import com.tianbao.buy.service.CoachService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class CoachServiceImpl extends AbstractService<Coach> implements CoachService {
    @Resource
    private CoachMapper coachMapper;

}
