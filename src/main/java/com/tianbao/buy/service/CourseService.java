package com.tianbao.buy.service;

import com.tianbao.buy.domain.Course;
import com.tianbao.buy.vo.CourseVO;
import com.tianbao.buy.vo.ScheduleVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CourseService {
    ScheduleVO schedule(String date, int num);

    Course getCourse(long id);

    CourseVO getCourse(long id, boolean isDetail);

    Map<Long, Course> getNormalCourse();

    Map<Long, Course> getCourse(Set<Long> ids);

    CourseVO convert2CourseVO(Course course, boolean filter, boolean hasAddress);

    List<CourseVO> convert2CourseVO(List<Course> courses);
}
