package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YenCareVO {
    private Long cardId;

    private String bgImage;

    private String gift;

    private String cash;

    private String total;

    private String discount;

    private List<CouponVO> couponVOs;
}
