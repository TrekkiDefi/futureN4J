package com.github.ittalks.commons.sdk.google.calendar.entity;

import com.google.api.services.calendar.model.CalendarListEntry;

/**
 * 同步接收到的日历
 * Created by zhaoyudong on 2017/3/7.
 */
public class ReceivedCalendarListEntry extends BaseEntity{

    private CalendarListEntry calendarListEntry;

    public CalendarListEntry getCalendarListEntry() {
        return calendarListEntry;
    }

    public void setCalendarListEntry(CalendarListEntry calendarListEntry) {
        this.calendarListEntry = calendarListEntry;
    }
}
