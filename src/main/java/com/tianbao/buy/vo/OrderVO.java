package com.tianbao.buy.vo;

import com.tianbao.buy.utils.enums.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private CourseVO courseVO;

    private PersonTime personTime;

    private List<PayDetail> payDetail;

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

    public enum Status implements EnumMessage {
        // 状态。0：软删除；1：待处理；2：已预约；3：已完成；4：已取消；5：冻结
        DEL((byte)0, "已删除"),
        PENDING((byte)1, "待处理"),
        ORDER((byte)2, "已预约"),
        END((byte)3, "已完成"),
        CANCLE((byte)4, "已取消"),
        BLOCKED((byte)5, "冻结");

        public byte code;

        public String desc;

        Status(byte code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public byte getCode() {
            return code;
        }

        public void setCode(byte code) {
            this.code = code;
        }

        @Override
        public Object getValue() {
            return code;
        }
    }
}
