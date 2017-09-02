package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.CourseDAO;
import com.tianbao.buy.domain.Course;
import com.tianbao.buy.manager.CourseManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/09/02.
 */
@Service
public class CourseManagerImpl extends AbstractManager<Course> implements CourseManager {
    @Resource
    private CourseDAO courseDAO;

}
