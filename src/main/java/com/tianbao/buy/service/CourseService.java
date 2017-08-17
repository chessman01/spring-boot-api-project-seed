package com.tianbao.buy.service;

import com.tianbao.buy.vo.CourseVO;
import com.tianbao.buy.vo.ScheduleVO;

public interface CourseService {
    ScheduleVO schedule(String date, int num);

    CourseVO detail(long id);
}
