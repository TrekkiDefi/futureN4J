package com.github.ittalks.commons.redis.queue.common;

/**
 * Created by 刘春龙 on 2017/3/5.
 */
public class TS {

    public static final String NORMAL = "normal";
    public static final String REPEAT = "repeat";

    public static final long TIMEOUT = 360 * 1000;// 单位：ms
    public static final long PROTECTED_TIME = 120 * 1000;// 单位：ms
}
