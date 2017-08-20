package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.manager.CouponUserManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tianbao.buy.service.impl.CouponServiceImpl;
import com.tianbao.buy.vo.CouponVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2017/08/15.
*/
@RestController
@RequestMapping("/coupon/user")
@SuppressWarnings("unchecked")
public class CouponUserRpc {
    @Resource
    private CouponServiceImpl couponService;

    @Resource
    private CouponUserManager couponUserManager;

    @PostMapping("/obtain")
    public Result obtain(@RequestParam(defaultValue = "0") long couponTemplateId) {
        couponService.obtain(couponTemplateId);
        return ResultGenerator.genSuccessResult("领券成功");
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<CouponUser> list = couponUserManager.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/test")
    public Result test() {
//        List<CouponVO> couponVOs = couponService.getCardRechargeTemplate();
//        List<CouponVO> couponVOs = couponService.getCoupon4Recharge(123l, 500000);

        List<CouponVO> couponVOs = couponService.getCoupon(123l, (byte) 1, null);

        return ResultGenerator.genSuccessResult(couponVOs);
    }
}
