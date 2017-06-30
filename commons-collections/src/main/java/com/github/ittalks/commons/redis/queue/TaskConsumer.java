package com.github.ittalks.commons.redis.queue;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by 刘春龙 on 2017/3/6.
 *
 * 任务消费者
 */
public abstract class TaskConsumer implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 消费任务
     */
    public abstract void consume();
}
