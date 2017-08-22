package com.github.ittalks.commons.redis.queue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by 刘春龙 on 2017/3/3.
 */
public class ConfigManager {

    private static Properties redisConn;
    private static Properties redisQueue;

    public ConfigManager(Properties redisConn, Properties redisQueue) {
        ConfigManager.redisConn = redisConn;
        ConfigManager.redisQueue = redisQueue;
    }

    public static Properties getRedisConn() {
        return redisConn;
    }

    public static Properties getRedisQueue() {
        return redisQueue;
    }

}
