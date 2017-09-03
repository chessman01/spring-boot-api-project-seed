package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.service.WxPayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx")
public class WXTestRpc {
    @Resource
    private WxPayService wxPayService;

    @PostMapping("/arrival")
    public Result arrival(@RequestParam String orderId) {
        wxPayService.paySuccess(orderId);

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestParam String orderId) {
        wxPayService.cancel(orderId);

        return ResultGenerator.genSuccessResult();
    }
}
