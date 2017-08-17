package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private CourseVO courseVO;

    private PersonTime personTime;

    private PayDetail payDetail;

    private String realPay;

    private Button creatButton;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonTime{
        private String title;

        private int num;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayDetail{
        private String title;

        private String price;

        private Boolean isPay;
    }
}
