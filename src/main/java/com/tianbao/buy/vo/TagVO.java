package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVO {
    private Long id;
    private String name;

    public enum Type {
        // 类型。1：通用；2：课程
        NORMAL((byte)1, "通用"),
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
    }
}
