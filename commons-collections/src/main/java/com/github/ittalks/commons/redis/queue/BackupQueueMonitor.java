package com.github.ittalks.commons.redis.queue;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.queue.common.TS;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/5.
 * <p>
 * 备份队列监控
 * <p>
 * 默认60s执行一次循环，对超时任务作处理。
 */
@Component("backupQueueMonitor")
public class BackupQueueMonitor {

    public static final Logger logger = Logger.getLogger(BackupQueueMonitor.class.getName());

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void monitor() {

        BackupQueue backupQueue = null;
        Task task;

        try {
            backupQueue = TaskQueueManager.getBackupQueue(TaskQueueManager.BACK_UP_QUEUE);//备份队列

            if (backupQueue != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                logger.info("备份队列[" + TaskQueueManager.BACK_UP_QUEUE + "]监控开始：" + format.format(new Date()));
                task = backupQueue.popTask();
                while (task != null &&
                        !TaskQueueManager.BACK_UP_QUEUE.equals(task.getQueue()) &&
                        !RedisBackupQueue.MARKER.equals(task.getType())) {
                    /**
                     * 判断任务状态，分别处理
                     * 1. 任务超时，且重复次数大于repeat指定次数，则持久化到数据库
                     * 2. 任务超时，且重复次数小于等于repeat指定次数，则重新放入任务队列
                     * 最后，如果满足以上条件，同时删除备份队列中的该任务
                     */
                    //获取任务状态
                    Task.TaskState ts = task.getTs();
                    long taskTimeMillis = ts.getTimestamp();//任务的时间戳
                    long currentTimeMillis = System.currentTimeMillis();//当前时间戳
                    long intervalTimeMillis = currentTimeMillis - taskTimeMillis;//任务存活时间
                    //判断任务是否超时
                    if (intervalTimeMillis > TS.TIMEOUT) {
                        Task originTask = JSON.parseObject(JSON.toJSONString(task), Task.class);
                        //任务超时
                        if (ts.getRepeat() <= 3) {
                            //重新放入任务队列
                            //1.更新Task状态时间戳
                            ts.setTimestamp(System.currentTimeMillis());
                            //2.更新状态标记：repeat
                            ts.setState(TS.REPEAT);
                            //3.更新状态reapeat+1
                            ts.setRepeat(ts.getRepeat() + 1);
                            task.setTs(ts);
                            //4.放入任务队列的队首，优先处理
                            String queueName = task.getQueue();
                            TaskQueue taskQueue = TaskQueueManager.getTaskQueue(queueName);
                            taskQueue.pushTaskToHeader(task);

                        } else {
                            //TODO 持久化到数据库
                        }
                        //删除备份队列中的该任务
                        backupQueue.finishTask(originTask);
                    }
                    //继续从备份队列中取出任务，进入下一次循环
                    task = backupQueue.popTask();
                }
            }

        } catch (Throwable e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

    }
}
