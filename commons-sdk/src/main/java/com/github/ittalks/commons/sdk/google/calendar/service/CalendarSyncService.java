package com.github.ittalks.commons.sdk.google.calendar.service;

import com.google.api.services.calendar.model.Calendar;

/**
 * Created by 刘春龙 on 2017/2/20.
 */
public interface CalendarSyncService {
    /**
     * 执行`日历数据`的批量同步
     * <p>
     * 向`消息队列`中加入`同步日历消息`
     *
     * @param userid 用户ID
     */
    public void sync(String userid);
}
