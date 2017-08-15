package com.tianbao.buy.utils.enums;

import com.tianbao.buy.vo.CouponVO;

/**
 * Created by yangqi on 17/8/15.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(EnumUtil.getEnumObject((byte) 0, CouponVO.CouponStatus.class).getDesc());
    }
}
