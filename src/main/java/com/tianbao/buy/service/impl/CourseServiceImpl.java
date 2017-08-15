package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.CourseMapper;
import com.tianbao.buy.model.Course;
import com.tianbao.buy.service.CourseService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class CourseServiceImpl extends AbstractService<Course> implements CourseService {
    @Resource
    private CourseMapper courseMapper;

}
