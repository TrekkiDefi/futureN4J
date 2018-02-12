package com.github.ittalks.commons.sdk.google.calendar.enums;

/**
 * Created by 刘春龙 on 2017/3/7.
 */
public enum Queue {

    msg_queue("cal_msg_queue"),
    data_queue("cal_data_queue");

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
