package com.github.ittalks.commons.sdk.google.calendar.task.handler;

import com.github.ittalks.commons.redis.queue.TaskHandler;

import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/6.
 */
public class CalendarListEntryTaskHandler implements TaskHandler {
    private static final Logger logger = Logger.getLogger(CalendarListEntryTaskHandler.class.getName());

    @Override
    public void handle(String data) {
        logger.info("处理[CalendarListEntry]数据，数据：" + data);
    }
}
