package com.tianbao.buy.domain;

import com.tianbao.buy.vo.OrderVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Context {
    private User user;

    private YenCard card;

    private CouponTemplate template;

    private CouponTemplate coupon;

    private CouponUser couponUser;

    private Course course;

    private Map<String, OrderVO.PayDetail> payDetailMap;
}
