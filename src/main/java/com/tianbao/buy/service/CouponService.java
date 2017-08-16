package com.tianbao.buy.service;

import com.tianbao.buy.vo.CouponVO;

import java.util.List;

public interface CouponService {
    List<CouponVO> getCoupon4Recharge(long userId, int price);

    List<CouponVO> getCoupon4PayPerView(long userId, int price);

    List<CouponVO> getCareRechargeTemplate();

    List<CouponVO> getCoupon(long userId, byte status);
}
