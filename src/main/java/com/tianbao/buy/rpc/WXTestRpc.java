package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.WxPayService;
import com.tianbao.buy.service.impl.TestService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.RedisClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx")
public class WXTestRpc {
    private static final Logger LOG = LoggerFactory.getLogger(WXTestRpc.class);

    @Resource
    private WxPayService wxPayService;

    @Resource
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TestService testService;

    @PostMapping("/arrival")
    public Result arrival(@RequestParam String orderId, @RequestParam String openId,
                          @RequestParam(defaultValue = "zh_CN") String lang,@RequestParam String payOrderId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        wxPayService.paySuccess(orderId, user,payOrderId);

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestParam String orderId, @RequestParam String openId,
                         @RequestParam(defaultValue = "zh_CN") String lang,@RequestParam String payOrderId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        wxPayService.cancel(orderId, user,payOrderId);

        return ResultGenerator.genSuccessResult();
    }



    @GetMapping("/redisCache")
    public String redisCache() {
        redisClient.set("shanhy", "hello,shanhy", 100);
        LOG.info("getRedisValue = {}", redisClient.get("shanhy"));
        testService.testCache2("aaa", "bbb");
        return testService.testCache();
    }

}
