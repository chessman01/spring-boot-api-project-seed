package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.manager.OrderMainManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tianbao.buy.service.OrderService;
import com.tianbao.buy.service.impl.YenCardServiceImpl;
import com.tianbao.buy.vo.OrderVO;
import com.tianbao.buy.vo.YenCardVO;
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

    @PostMapping("/detail")
    public Result detail(@RequestParam String orderId) {
        OrderVO order = orderService.detail(orderId);
        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Byte status) {
        List<OrderVO> orders = orderService.get(status);
        return ResultGenerator.genSuccessResult(orders);
    }

    @PostMapping("/build")
    public Result build(@RequestParam long courseId) {
        OrderVO order = orderService.build(courseId);

        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/adjust")
    public Result adjust(@RequestParam long courseId, Long cardId, Long couponId,
                         @RequestParam int personTime) {
        OrderVO order = orderService.adjust(courseId, cardId, couponId, personTime);

        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/create")
    public Result create(@RequestParam long courseId, Long couponId, @RequestParam Byte personTime) {
        String orderId = orderService.create(courseId, couponId, personTime);

        return ResultGenerator.genSuccessResult(orderId);
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestParam String orderId) {
        orderService.cancel(orderId);
        return ResultGenerator.genSuccessResult();
    }
}
