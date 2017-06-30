package com.github.ittalks.commons.redis.queue.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by 刘春龙 on 2017/4/13.
 *
 * Redis连接获取失败，执行重试
 */
public class RetryPolicy {

    private static Logger logger = LoggerFactory.getLogger(RetryPolicy.class);
    private static final long internal = 60l;//重试增长间隔：1分钟

    public static void tsleep() {
        try {
            logger.error("获取Redis连接失败，" + internal + "s后重试");
            TimeUnit.SECONDS.sleep(internal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
