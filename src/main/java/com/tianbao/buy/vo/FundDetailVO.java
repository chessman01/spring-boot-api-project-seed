package com.tianbao.buy.vo;

import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.utils.enums.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundDetailVO extends FundDetail{
    // 这里直接继承了FundDetail，不需要对字段进行转义给前端

    public enum Channel implements EnumMessage {
        // 支付通道。0：瘾卡；1：微信
        YENCARD((byte)0, "瘾卡"),
        WEIXIN((byte)1, "微信");

        public byte code;

        public String desc;

        Channel(byte code, String desc) {
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

    public enum Target implements EnumMessage {
        // 操作目标。0：瘾卡；1：按次消费
        CARD((byte)0, "瘾卡"),
        PAY_PER_VIEW((byte)1, "按次消费");

        public byte code;

        public String desc;

        Target(byte code, String desc) {
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

    public enum Direction implements EnumMessage {
        // 流动方向。0：支出；1：进账；
        OUT((byte)0, "支出"),
        IN((byte)1, "进账");

        public byte code;

        public String desc;

        Direction(byte code, String desc) {
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

    public enum AccountType implements EnumMessage {
        // 账号类型。1：现金；2：赠送
        CASH((byte)0, "现金"),
        GIFT((byte)1, "赠送");

        public byte code;

        public String desc;

        AccountType(byte code, String desc) {
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
        // 状态。0：软删除；1：待处理；2：已处理；3：冻结
        DEL((byte)0, "已删除"),
        PENDING((byte)1, "待处理"),
        FINISH((byte)2, "已处理"),
        BLOCKED((byte)3, "冻结");

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
