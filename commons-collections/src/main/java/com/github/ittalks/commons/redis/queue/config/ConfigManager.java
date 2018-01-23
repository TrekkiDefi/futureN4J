package com.github.ittalks.commons.redis.queue.config;

import java.util.Properties;

/**
 * Created by 刘春龙 on 2017/3/3.
 */
public class ConfigManager {

    private static Properties redisConn;
    private static String queues;
    private static int queueRetry;

    /**
     * 设置redis连接属性，redis队列
     *
     * @param redisConn   redis连接属性
     * @param queues      redis队列
     * @param queueRetry 异常中断任务重试次数
     */
    public ConfigManager(Properties redisConn, String queues, int queueRetry) {
        ConfigManager.redisConn = redisConn;
        ConfigManager.queues = queues;
        ConfigManager.queueRetry = queueRetry;
    }

    public static void setQueueRetry(int queueRetry) {
        ConfigManager.queueRetry = queueRetry;
    }

    public static Properties getRedisConn() {
        return redisConn;
    }

    public static String getQueues() {
        return queues;
    }

    public static int getQueueRetry() {
        return queueRetry;
    }
}
