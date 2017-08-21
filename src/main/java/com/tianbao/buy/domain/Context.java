package com.tianbao.buy.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Context {
    private User user;

    private YenCard card;

    private CouponTemplate template;

    private CouponTemplate coupon;

    private CouponUser couponUser;

    private Course course;
}
