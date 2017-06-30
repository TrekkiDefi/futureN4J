package com.github.ittalks.commons.sdk.google.calendar.enums;

/**
 * Created by 刘春龙 on 2017/3/7.
 */
public enum Queue {
    MS_QUEUE("MS_QUEUE$102e5239-d499-4eee-b815-50bd46f7c084"),
    DAT_QUEUE("DAT_QUEUE$102e5239-d499-4eee-b815-50bd46f7c084");

    Queue(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
