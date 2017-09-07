package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.service.CourseService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.vo.CourseVO;
import com.tianbao.buy.vo.ScheduleVO;
import me.chanjar.weixin.common.exception.WxErrorException;
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

    @Resource
    private UserService userService;

    @PostMapping("/schedule")
    public Result schedule(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                           String date, @RequestParam(defaultValue = "7") Integer num) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        ScheduleVO scheduleVO = courseService.schedule(date, num, user);
        return ResultGenerator.genSuccessResult(scheduleVO);
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam Long id) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        CourseVO courseVO = courseService.getCourse(id, true, user);
        return ResultGenerator.genSuccessResult(courseVO);
    }
}
