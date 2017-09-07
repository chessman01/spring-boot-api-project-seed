package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.manager.OrderMainManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tianbao.buy.service.OrderService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.impl.YenCardServiceImpl;
import com.tianbao.buy.vo.OrderVO;
import com.tianbao.buy.vo.YenCardVO;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderRpc {
    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @PostMapping("/detail")
    public Result detail(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam String orderId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        OrderVO order = orderService.detail(orderId, user);
        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/list")
    public Result list(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                       @RequestParam(defaultValue = "1") Byte status) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        List<OrderVO> orders = orderService.get(status, user);
        return ResultGenerator.genSuccessResult(orders);
    }

    @PostMapping("/build")
    public Result build(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                        @RequestParam long courseId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        OrderVO order = orderService.build(courseId, user);

        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/adjust")
    public Result adjust(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam long courseId, Long cardId, Long couponId, @RequestParam int personTime)
            throws WxErrorException{
        User user = userService.getUserByWxOpenId(openId, lang);
        OrderVO order = orderService.adjust(courseId, cardId, couponId, personTime, user);

        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/create")
    public Result create(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam long courseId, Long couponId, @RequestParam Byte personTime) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        String orderId = orderService.create(courseId, couponId, personTime, user);

        return ResultGenerator.genSuccessResult(orderId);
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam String orderId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        orderService.cancel(orderId, user);
        return ResultGenerator.genSuccessResult();
    }
}
