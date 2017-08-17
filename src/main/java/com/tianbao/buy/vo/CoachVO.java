package com.tianbao.buy.vo;

import com.tianbao.buy.utils.enums.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachVO {
    private Long id;

    private String nick;

    private String phone;

    private String desc;

    private String avatar;

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

    public enum Sex implements EnumMessage {
        // 性别。1：男；0：女
        MALE((byte)1, "男"),
        FEMALE((byte)0, "女");

        public byte code;

        public String desc;

        Sex(byte code, String desc) {
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
