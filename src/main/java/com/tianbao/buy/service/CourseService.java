package com.tianbao.buy.service;

import com.tianbao.buy.vo.CourseVO;
import com.tianbao.buy.vo.ScheduleVO;

import java.util.Map;
import java.util.Set;

public interface CourseService {
    ScheduleVO schedule(String date, int num);

    CourseVO detail(long id);

    Map<Long, CourseVO> getNormalCourse();

    Map<Long, CourseVO> getCourse(Set<Long> ids);
}
