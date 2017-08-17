package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.Course;
import com.tianbao.buy.manager.CourseManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tianbao.buy.service.CourseService;
import com.tianbao.buy.vo.ScheduleVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2017/08/15.
*/
@RestController
@RequestMapping("/course")
public class CourseRpc {
    @Resource
    private CourseService courseService;

    @PostMapping("/schedule")
    public Result schedule(String date, @RequestParam(defaultValue = "7") Integer num) {
        ScheduleVO scheduleVO = courseService.getSchedule(date, num);
        return ResultGenerator.genSuccessResult(scheduleVO);
    }
}
