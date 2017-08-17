package com.tianbao.buy.service;

import com.tianbao.buy.vo.ScheduleVO;

public interface CourseService {
    ScheduleVO getSchedule(String date, int num);
}
