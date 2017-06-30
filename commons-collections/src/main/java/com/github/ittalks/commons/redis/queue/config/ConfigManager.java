package com.github.ittalks.commons.redis.queue.config;

import java.util.Properties;

/**
 * Created by 刘春龙 on 2017/3/3.
 */
public class ConfigManager {

    private static Properties redisConn;
    private static Properties redisQueue;

    public void setRedisConn(Properties redisConn) {
        ConfigManager.redisConn = redisConn;
    }

    public void setRedisQueue(Properties redisQueue) {
        ConfigManager.redisQueue = redisQueue;
    }

    public static Properties getRedisConn() {
        return redisConn;
    }

    public static Properties getRedisQueue() {
        return redisQueue;
    }
}
