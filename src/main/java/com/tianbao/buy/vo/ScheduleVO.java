package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleVO {
    private List<Button> banner;

    private List<Date> calendar;

    private Address address;

    private List<CourseVO> course;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Date {
        private String dayOfWeek;

        private String monthDayFormat;

        private String dayOfWeekDetail;

        private String yearMonthDayFormat;

        private boolean selected;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String longitude;

        private String latitude;

        private String detailAddress;

        private String name;
    }
}
