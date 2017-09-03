package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.CoachDAO;
import com.tianbao.buy.domain.Coach;
import com.tianbao.buy.manager.CoachManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/09/03.
 */
@Service
public class CoachManagerImpl extends AbstractManager<Coach> implements CoachManager {
    @Resource
    private CoachDAO coachDAO;

}
