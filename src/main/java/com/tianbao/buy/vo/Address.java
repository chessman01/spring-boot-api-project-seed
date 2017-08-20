package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String longitude;

    private String latitude;

    private String detailAddress;

    private String name;
}