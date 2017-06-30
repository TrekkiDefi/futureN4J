package com.github.ittalks.commons.sdk.google.calendar.task;

import com.github.ittalks.commons.sdk.google.calendar.task.handler.CalendarListEntryTaskHandler;
import com.github.ittalks.commons.sdk.google.calendar.task.handler.EventTaskHandler;
import com.github.ittalks.commons.sdk.google.calendar.task.handler.SynCalendarListTaskHandler;
import com.github.ittalks.commons.sdk.google.calendar.task.handler.SynEventsTaskHandler;

/**
 * Created by 刘春龙 on 2017/3/6.
 */
public enum TaskType {
    CALENDARLIST("CALENDARLIST", CalendarListEntryTaskHandler.class), EVENT("EVENT", EventTaskHandler.class),
    SYN_CALENDARLIST("SYN_CALENDARLIST", SynCalendarListTaskHandler.class),
    SYN_EVENTS("SYN_EVENTS", SynEventsTaskHandler.class);

    TaskType(String type, Class taskHandler) {
        this.type = type;
        this.taskHandler = taskHandler;
    }

    private String type;
    private Class taskHandler;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class getTaskHandler() {
        return taskHandler;
    }

    public void setTaskHandler(Class taskHandler) {
        this.taskHandler = taskHandler;
    }
}
