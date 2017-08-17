package com.tianbao.buy.vo;

import com.tianbao.buy.utils.enums.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseVO {
    private Long id;

    private String title;

    private String address;

    private Date startTime;

    private Date endTime;

    private Integer price;

    private String desc;

    private List<TagVO> tagName;

    private List<String> mainPics;

    private List<String> subPics;

    private String trainingEffect;

    private String crowd;

    private String faq;

    private String care;

    private CoachVO coachVO;

    public enum Status implements EnumMessage {
        // 状态。0：软删除；1：正常
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

        @Override
        public Object getValue() {
            return code;
        }
    }
}
