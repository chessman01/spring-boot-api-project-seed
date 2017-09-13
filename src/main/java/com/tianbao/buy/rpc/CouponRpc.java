package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.vo.CouponVO;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/coupon")
@SuppressWarnings("unchecked")
public class CouponRpc {
    @Resource
    private CouponService couponService;

    @Resource
    private UserService userService;

    @PostMapping("/obtain")
    public Result obtain(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam long couponTemplateId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        couponService.obtain(user.getId(), couponTemplateId, (byte)1);
        return ResultGenerator.genSuccessResult("领券成功.");
    }

    @PostMapping("/list")
    public Result list(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                       @RequestParam(defaultValue = "1") Byte status) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        //只获取状态为1，2，3
        if (status==1||status==2||status==3) {
            List<CouponVO> couponVOs = couponService.getCoupon(status, user);
            return ResultGenerator.genSuccessResult(couponVOs);
        } else {
            return ResultGenerator.genFailResult("参数不合法");
        }
    }
}
