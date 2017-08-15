package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponVO {
    private Long id;

    private Integer price;

    private String sourceDesc;

    private String startTime;

    private String endTime;

    private Byte rule;

    private Integer rulePrice;

    private Byte payType;

    private Byte status;

    public enum Abc{

    }
}



