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

//    @PostMapping("/list")
//    public Result list() {
//        List<YenCardVO> voList = yenCardServiceImpl.getCardByUser();
//
//        return ResultGenerator.genSuccessResult(voList);
//    }

    @PostMapping("/build")
    public Result build(@RequestParam(defaultValue = "0") long courseId) {
        OrderVO order = orderService.build(courseId);

        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/adjust")
    public Result adjust(long courseId, Long cardId, Long couponId, int personTime) {
        OrderVO order = orderService.adjust(courseId, cardId, couponId, personTime);

        return ResultGenerator.genSuccessResult(order);
    }

    @PostMapping("/create")
    public Result create(long courseId, long couponId, int personTime, long cardId) {
        String url = orderService.create(courseId, couponId, personTime, cardId);

        return ResultGenerator.genSuccessResult(url);
    }
}
