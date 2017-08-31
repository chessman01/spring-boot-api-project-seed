package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private CourseVO course;

    private List<PersonTime> personTime;

    private List<PayDetail> payDetail;

    private PayDetail realPay;

    private Button button;

    private Order order;

    private List<CouponVO> coupon;

    private YenCardVO card;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order{
        private String orderId;

        private String createTime;

        private Long id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonTime{
        private String title;

        private Integer num;

        private boolean isSelect;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayDetail{
        private String title;

        private String fee;

        private int originFee;
    }

    public enum Type {
        // 类型。1：瘾卡；2：课程
        CARD((byte)1, "瘾卡"),
        COURSE((byte)2, "课程");

        public byte code;

        public String desc;

        Type(byte code, String desc) {
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
    }

    public enum Status {
        // 状态。0：软删除；1：待支付；2：已预约；3：已完成；4：待取消；5：已取消；6：冻结
        DEL((byte)0, "已删除"),
        PENDING_PAY((byte)1, "待支付"),
        ORDER((byte)2, "已预约"),
        END((byte)3, "已完成"),
        PENDING_CANCLE((byte)4, "待取消"),
        CANCLED((byte)5, "已取消"),
        BLOCKED((byte)6, "冻结");

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
    }
}
