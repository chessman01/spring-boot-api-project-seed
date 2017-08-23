package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private long userId;

    private String durationTotal;

    private String durationWeek;

    private String calorieTotal;

    private String point;

    public enum Status {
        // 状态。1：正常；0：软删除
        DEL((byte)0, "已删除"),
        NORMAL((byte)1, "正常");

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
