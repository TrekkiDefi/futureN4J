package com.github.ittalks.commons.sdk.google.calendar.entity;

/**
 * Created by zhaoyudong on 2017/3/7.
 */
public class SyncCalendarListEntity extends BaseEntity{

    private boolean only;

    public boolean isOnly() {
        return only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }
}
