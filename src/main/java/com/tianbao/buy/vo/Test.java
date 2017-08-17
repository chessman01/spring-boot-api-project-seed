package com.tianbao.buy.vo;

import com.tianbao.buy.utils.enums.EnumUtil;

/**
 * Created by yangqi on 17/8/16.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(EnumUtil.getEnumObject((byte) 1, CouponVO.Source.class).getDesc());
    }
}
