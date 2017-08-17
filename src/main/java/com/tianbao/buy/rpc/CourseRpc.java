package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.service.CourseService;
import com.tianbao.buy.vo.CourseVO;
import com.tianbao.buy.vo.ScheduleVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/course")
public class CourseRpc {
    @Resource
    private CourseService courseService;

    @PostMapping("/schedule")
    public Result schedule(String date, @RequestParam(defaultValue = "7") Integer num) {
        ScheduleVO scheduleVO = courseService.schedule(date, num);
        return ResultGenerator.genSuccessResult(scheduleVO);
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Long id) {
        CourseVO courseVO = courseService.detail(id);
        return ResultGenerator.genSuccessResult(courseVO);
    }
}
