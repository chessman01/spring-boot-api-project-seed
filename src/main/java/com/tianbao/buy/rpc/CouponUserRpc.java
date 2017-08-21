package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.vo.CouponVO;
import com.tianbao.buy.vo.InvitationVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/coupon/user")
@SuppressWarnings("unchecked")
public class CouponUserRpc {
    @Resource
    private CouponService couponService;

    @Resource
    private UserService userService;

    @PostMapping("/obtain")
    public Result obtain(@RequestParam(defaultValue = "0") long couponTemplateId) {
        couponService.obtain(couponTemplateId);
        return ResultGenerator.genSuccessResult("领券成功.");
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Byte status) {
        List<CouponVO> couponVOs = couponService.getCoupon(status);
        return ResultGenerator.genSuccessResult(couponVOs);
    }

    @PostMapping("/invitation")
    public Result invitation() {
        InvitationVO invitation = userService.invitation();
        return ResultGenerator.genSuccessResult(invitation);
    }
}
