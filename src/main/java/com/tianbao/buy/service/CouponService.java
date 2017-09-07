package com.tianbao.buy.service;

import com.tianbao.buy.domain.Context;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.vo.CouponVO;
import me.chanjar.weixin.common.exception.WxErrorException;

import java.util.List;
import java.util.Set;

public interface CouponService {
    List<CouponVO> getCoupon4Recharge(long userId, int price, Long selectId, Context context);

    List<CouponVO> getCoupon4PayPerView(long userId, int price, Long selectId, Context context);

    List<CouponVO> getCardRechargeTemplate(Context context, Long selectId);

    List<CouponVO> getCoupon(byte status, User user) throws WxErrorException;

    CouponUser getCouponUser(long id);

    CouponTemplate getTemplate(long id);

    void obtainRecommend(long templateId, long userId, Byte from);

    void obtain(long userId, long templateId, Byte from) throws WxErrorException;

    void obtain(long couponTemplateId, Set<Byte> sourceSet, Byte from, long userId);

    void updateCouponUserStatus(long recordId, byte newStatus, byte originStatus);

    CouponTemplate getRecommendTemplate();

    int getCouponNum(long couponTemplateId, long userId);
}
