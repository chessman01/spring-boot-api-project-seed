package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.WxPayService;
import me.chanjar.weixin.common.exception.WxErrorException;
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

    @Resource
    private UserService userService;

    @PostMapping("/arrival")
    public Result arrival(@RequestParam String orderId, @RequestParam String openId,
                          @RequestParam(defaultValue = "zh_CN") String lang) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        wxPayService.paySuccess(orderId, user);

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestParam String orderId, @RequestParam String openId,
                         @RequestParam(defaultValue = "zh_CN") String lang) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        wxPayService.cancel(orderId, user);

        return ResultGenerator.genSuccessResult();
    }
}
