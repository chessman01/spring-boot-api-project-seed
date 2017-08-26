package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.vo.CouponVO;
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

    @PostMapping("/obtain")
    public Result obtain(@RequestParam long couponTemplateId) {
        couponService.obtain(couponTemplateId, (byte)1);
        return ResultGenerator.genSuccessResult("领券成功.");
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Byte status) {
        List<CouponVO> couponVOs = couponService.getCoupon(status);
        return ResultGenerator.genSuccessResult(couponVOs);
    }


}
