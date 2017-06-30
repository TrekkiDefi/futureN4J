package com.github.ittalks.commons.redis.queue;

/**
 * Created by 刘春龙 on 2017/3/6.
 */
public interface TaskHandler {

    public void handle(String data);
}
