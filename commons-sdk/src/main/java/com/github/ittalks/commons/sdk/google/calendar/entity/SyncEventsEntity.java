package com.github.ittalks.commons.sdk.google.calendar.entity;

/**
 * 同步事件的实体
 * Created by zhaoyudong on 2017/3/7.
 */
public class SyncEventsEntity extends BaseEntity{
    /**
     * 对应日历Id
     */
    private String calendarId;
    /**
     * 资源URI
     */
    private String uri;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
