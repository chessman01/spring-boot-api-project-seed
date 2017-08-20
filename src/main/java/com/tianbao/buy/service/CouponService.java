package com.tianbao.buy.service;

import com.tianbao.buy.domain.Context;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.vo.CouponVO;

import java.util.List;

public interface CouponService {
    List<CouponVO> getCoupon4Recharge(long userId, int price, Long selectId, Context context);

    List<CouponVO> getCoupon4PayPerView(long userId, int price, Long selectId, Context context);

    List<CouponVO> getCardRechargeTemplate(Context context, Long selectId);

    List<CouponVO> getCoupon(long userId, byte status, Context context);

    CouponUser getCouponUser(long id);

    CouponTemplate getTemplate(long id);

    void obtain(Long couponTemplateId);
}
