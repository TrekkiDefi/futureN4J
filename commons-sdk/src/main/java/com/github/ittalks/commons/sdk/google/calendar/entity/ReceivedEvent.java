package com.github.ittalks.commons.sdk.google.calendar.entity;

import com.google.api.services.calendar.model.Event;

/**
 * 同步接收到的事件
 * Created by zhaoyudong on 2017/3/7.
 */
public class ReceivedEvent extends BaseEntity {
    private String calendarId;

    private Event event;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
