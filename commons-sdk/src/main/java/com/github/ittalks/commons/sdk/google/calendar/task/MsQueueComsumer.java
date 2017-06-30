package com.github.ittalks.commons.sdk.google.calendar.task;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.queue.Task;
import com.github.ittalks.commons.redis.queue.TaskConsumer;
import com.github.ittalks.commons.redis.queue.TaskQueue;
import com.github.ittalks.commons.redis.queue.TaskQueueManager;
import com.github.ittalks.commons.redis.queue.common.RetryPolicy;
import com.github.ittalks.commons.sdk.google.calendar.enums.Queue;
import com.github.ittalks.commons.sdk.google.calendar.task.tpool.ExecutorProcessPool;
import com.github.ittalks.commons.sdk.google.calendar.task.tpool.MsExecutorProcessPool;
import org.springframework.context.event.ContextRefreshedEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/6.
 */
public class MsQueueComsumer extends TaskConsumer {

    public static final Logger logger = Logger.getLogger(MsQueueComsumer.class.getName());

    private MsExecutorProcessPool msPool = MsExecutorProcessPool.getInstance();
    private ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
    @Override
    public void consume() {
        TaskQueue taskQueue = null;

        try {
            taskQueue = TaskQueueManager.getTaskQueue(Queue.MS_QUEUE.getName());

            if (taskQueue != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                logger.info("队列[" + Queue.MS_QUEUE.getName() + "]消费开始：" + format.format(new Date()));

                while (true) {

                    final Task task = taskQueue.popTask();//阻塞方式获取任务

                    if (task == null) {
                        //获取队列任务失败，采取重试策略
                        RetryPolicy.tsleep();
                        continue;
                    }

                    if (!Queue.MS_QUEUE.getName().equals(task.getQueue())) {
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
            msPool.execute(new Runnable() {
                @Override
                public void run() {
                    consume();
                }
            });
        }

    }
}
