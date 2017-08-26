package com.tianbao.buy.service;

import com.tianbao.buy.domain.Context;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.vo.CouponVO;

import java.util.List;
import java.util.Set;

public interface CouponService {
    List<CouponVO> getCoupon4Recharge(long userId, int price, Long selectId, Context context);

    List<CouponVO> getCoupon4PayPerView(long userId, int price, Long selectId, Context context);

    List<CouponVO> getCardRechargeTemplate(Context context, Long selectId);

    List<CouponVO> getCoupon(byte status);

    CouponUser getCouponUser(long id);

    CouponTemplate getTemplate(long id);

    void obtainRecommend(long templateId, long userId, Byte from);

    void obtain(long couponTemplateId, Byte from);

    void obtain(long couponTemplateId, Set<Byte> sourceSet, long userId, Byte from);

    void updateCouponUserStatus(long recordId, byte newStatus, byte originStatus);

    CouponTemplate getRecommendTemplate();

    int getCouponNum(long couponTemplateId, long userId);
}
