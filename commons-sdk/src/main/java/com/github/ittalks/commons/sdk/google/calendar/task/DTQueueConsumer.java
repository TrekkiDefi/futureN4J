package com.github.ittalks.commons.sdk.google.calendar.task;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.queue.Task;
import com.github.ittalks.commons.redis.queue.TaskConsumer;
import com.github.ittalks.commons.redis.queue.TaskQueue;
import com.github.ittalks.commons.redis.queue.TaskQueueManager;
import com.github.ittalks.commons.redis.queue.common.RetryPolicy;
import com.github.ittalks.commons.sdk.google.calendar.enums.Queue;
import com.github.ittalks.commons.sdk.google.calendar.task.pool.DTExecutorProcessPool;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/6.
 */
@DependsOn("taskQueueManager")
@Component
public class DTQueueConsumer implements TaskConsumer, ApplicationListener<ContextRefreshedEvent> {

    public static final Logger logger = Logger.getLogger(DTQueueConsumer.class.getName());

    private DTExecutorProcessPool dtPool = DTExecutorProcessPool.getInstance();

    @Override
    public void consume() {
        TaskQueue taskQueue = null;

        try {
            taskQueue = TaskQueueManager.getTaskQueue(Queue.DT_QUEUE.getName());

            if (taskQueue != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                logger.info("队列[" + Queue.DT_QUEUE.getName() + "]消费开始：" + format.format(new Date()));

                while (true) {

                    final Task task = taskQueue.popTask();

                    if (task == null) {
                        //获取队列任务失败，采取重试策略
                        RetryPolicy.tsleep();
                        continue;
                    }

                    if (!Queue.DT_QUEUE.getName().equals(task.getQueue())) {
                        //不是数据队列任务，不处理
                        continue;
                    }

                    for (TaskType type : TaskType.values()) {
                        try {
                            if (type.getType().equals(task.getType())) {
                                logger.info("消费任务：" + JSON.toJSONString(task));
                                task.doTask(type.getTaskHandler());
                                break;//跳出TaskHandler匹配
                            }
                        }  catch (Throwable e) {
                            logger.info(e.getMessage());
                        }
                    }

                }
            }

        } catch (Throwable e) {
            logger.info(e.getMessage());
        }

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            dtPool.execute(new Runnable() {
                @Override
                public void run() {
                    consume();
                }
            });
        }

    }
}
