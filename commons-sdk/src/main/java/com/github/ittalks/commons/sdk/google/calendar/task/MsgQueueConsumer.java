package com.github.ittalks.commons.sdk.google.calendar.task;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.sdk.google.calendar.enums.Queue;
import com.github.ittalks.commons.thread.pool.ExecutorProcessPool;
import com.kingsoft.wps.mail.queue.KMQueueManager;
import com.kingsoft.wps.mail.queue.Task;
import com.kingsoft.wps.mail.queue.TaskQueue;
import org.springframework.beans.factory.annotation.Autowired;
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
@DependsOn("calKMQueueManager")
@Component
public class MsgQueueConsumer implements TaskConsumer, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = Logger.getLogger(MsgQueueConsumer.class.getName());

    private ExecutorProcessPool processPool = ExecutorProcessPool.getInstance();

    private final KMQueueManager calKMQueueManager;

    @Autowired
    public MsgQueueConsumer(KMQueueManager calKMQueueManager) {
        this.calKMQueueManager = calKMQueueManager;
    }

    @Override
    public void consume() {
        TaskQueue taskQueue;

        try {
            taskQueue = calKMQueueManager.getTaskQueue(Queue.msg_queue.getName());

            if (taskQueue != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                logger.info("队列[" + Queue.msg_queue.getName() + "]消费开始：" + format.format(new Date()));

                while (true) {

                    final Task task = taskQueue.popTask();//阻塞方式获取任务

                    if (task == null) {
                        // 获取队列任务失败，采取重试策略
                        // 一般由于连接redis失败导致，稍后重试
                        RetryPolicy.sleep();
                        continue;
                    }

                    if (!Queue.msg_queue.getName().equals(task.getQueue())) {
                        continue;
                    }

                    for (TaskType type : TaskType.values()) {
                        try {
                            if (type.getType().equals(task.getType())) {
                                logger.info("消费任务：" + JSON.toJSONString(task));
                                task.doTask(calKMQueueManager, type.getTaskHandler());
                                break;
                            }
                        } catch (Throwable e) {
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
            for (int i = 0; i < 5; i++) {
                processPool.execute(this::consume);
            }
        }

    }
}
