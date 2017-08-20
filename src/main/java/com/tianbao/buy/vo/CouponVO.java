package com.tianbao.buy.vo;

import com.tianbao.buy.utils.enums.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponVO {
    private Long id;

    private Long couponUserId;

    private Integer originPrice;

    private String price;

    private String sourceDesc;

    private String time;

    private String rule;

    private String rulePrice;

    private String payType;

    private String status;

    private boolean selected;

    public enum VlidityUnit implements EnumMessage {
        // 有效期内容。1：天；2：周；3：月
        DAY((byte)1, "天"),
        WEEK((byte)2, "周"),
        MONTH((byte)3, "月");

        public byte code;

        public String desc;

        VlidityUnit(byte code, String desc) {
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

    public enum Source implements EnumMessage {
        // 来源类型。1：邀请好友；2：线下领取；3：微信领取；4：系统发放
        FRIEND((byte)1, "邀请好友"),
        OFFLINE((byte)2, "线下领取"),
        WEIXIN((byte)3, "微信领取"),
        SYS((byte)3, "系统发放");

        public byte code;

        public String desc;

        Source(byte code, String desc) {
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

    public enum Rule implements EnumMessage {
        // 规则。1：满送；2:抵扣
        GIVE((byte)1, "满送"),
        REDUCE((byte)2, "抵扣");

        public byte code;

        public String desc;

        Rule(byte code, String desc) {
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

    public enum PayType implements EnumMessage {
        // 支付类型。1：瘾卡充值；2：按次支付；
        RECHARGE((byte)1, "瘾卡充值"),
        PAY_PER_VIEW((byte)2, "按次支付"),
        ALL((byte)3, "所有");

        public byte code;

        public String desc;

        PayType(byte code, String desc) {
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

    public enum Status implements EnumMessage {
        // 状态。0：删除；1：正常；2：已过期；3：已使用
        DEL((byte)0, "已删除"),
        NORMAL((byte)1, "正常"),
        EXPIRED((byte)2, "已过期"),
        USED((byte)3, "已使用"),
        RECHARGE((byte)8, "充值瘾卡赠送模版-保留");

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



