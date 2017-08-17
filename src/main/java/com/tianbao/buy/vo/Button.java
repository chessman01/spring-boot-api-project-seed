package com.tianbao.buy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Button {
    private Boolean selected;

    private String title;

    private Event event;

    private String icon;

    private Boolean disable = true;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event {
        private String url;

        private String type = "click";
    }
}
