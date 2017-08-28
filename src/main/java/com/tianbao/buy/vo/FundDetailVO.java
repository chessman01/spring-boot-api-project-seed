package com.tianbao.buy.vo;

import com.tianbao.buy.domain.FundDetail;
import lombok.Data;

@Data
public class FundDetailVO extends FundDetail{
    // 这里直接继承了FundDetail，不需要对字段进行转义给前端
    public enum Channel {
        // 支付通道。0：瘾卡；1：微信；2：礼券；3：赠送；4：立减；8：结束
        YENCARD((byte)0, "瘾卡"),
        WEIXIN((byte)1, "微信"),
        COUPON((byte)2, "礼券"),
        GIFT((byte)3, "赠送"),
        REDUCE((byte)4, "立减"),
        END((byte)8, "结束");

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
    }

    public enum Direction {
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
    }

    public enum Status {
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
    }
}
