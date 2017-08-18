package com.tianbao.buy.vo;

import com.tianbao.buy.utils.enums.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YenCardVO {
    private Long cardId;

    private String bgImage;

    private String gift;

    private String cash;

    private String total;

    private String discount;

    private String rechargeUrl;

    private List<CouponVO> couponVOs;

    private List<CouponVO> templates;

    private Button button;

    public enum Type implements EnumMessage {
        // 类型。1：普通；2：情侣
        NORMAL((byte)1, "正常"),
        LOVERS((byte)2, "情侣");

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

        @Override
        public Object getValue() {
            return code;
        }
    }

    public enum Status implements EnumMessage {
        // 状态。0：软删除；1:正常；2：冻结
        DEL((byte)0, "已删除"),
        NORMAL((byte)1, "正常"),
        BLOCKED((byte)2, "冻结");

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
