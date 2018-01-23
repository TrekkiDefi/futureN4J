package com.github.ittalks.commons.redis.queue.common;

/**
 * Created by 刘春龙 on 2017/3/5.
 */
public class Constant {

    /**
     * 标记任务为正常执行状态
     */
    public static final String NORMAL = "normal";

    /**
     * 标记任务为重复执行状态
     */
    public static final String RETRY = "retry";

    /**
     * 任务的存活超时时间，单位：ms
     *
     * 该值只针对安全队列起作用
     */
    public static final long ALIVE_TIMEOUT = 5 * 60 * 1000;

    /**
     * 任务执行的超时时间（一次执行），单位：ms
     *
     * 该值只针对安全队列起作用
     *
     * TODO 后续会加入健康检测
     */
    public static final long PROTECTED_TIMEOUT = 2 * 60 * 1000;
}
