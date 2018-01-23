package com.github.ittalks.commons.redis.queue;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.queue.common.Constant;
import com.github.ittalks.commons.redis.queue.config.ConfigManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/5.
 * <p>
 * 备份队列监控
 * <p>
 * 超时任务重试
 */
public class BackupQueueMonitor {

    private static final Logger logger = Logger.getLogger(BackupQueueMonitor.class.getName());

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void monitor() {

        BackupQueue backupQueue = null;
        Task task;

        try {
            backupQueue = TaskQueueManager.getBackupQueue();// 备份队列

            if (backupQueue != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                logger.info("Backup queue[" + TaskQueueManager.BACK_UP_QUEUE + "]Monitoring begins：" + format.format(new Date()));
                task = backupQueue.popTask();
                while (task != null &&
                        !TaskQueueManager.BACK_UP_QUEUE.equals(task.getQueue()) &&
                        !RedisBackupQueue.MARKER.equals(task.getType())) {
                    /**
                     * 判断任务状态，分别处理
                     * 1. 任务执行超时，且重试次数大于等于retry指定次数，则持久化到数据库
                     * 2. 任务执行超时，且重试次数小于retry指定次数，则重新放入任务队列
                     * 最后，如果满足以上条件，同时删除备份队列中的该任务
                     */
                    // 获取任务状态
                    Task.TaskStatus status = task.getTaskStatus();

                    long currentTimeMillis = System.currentTimeMillis();// 当前时间戳
                    long taskGenTimeMillis = status.getGenTimestamp();// 任务生成的时间戳
                    long intervalTimeMillis = currentTimeMillis - taskGenTimeMillis;// 任务的存活时间
                    if (intervalTimeMillis > Constant.ALIVE_TIMEOUT) {

                        // TODO 持久化到数据库

                        // 删除备份队列中的该任务
                        backupQueue.finishTask(task);
                    }

                    long taskExcTimeMillis = status.getExcTimestamp();// 任务执行的时间戳
                    intervalTimeMillis = currentTimeMillis - taskExcTimeMillis;// 任务此次执行时间

                    if (intervalTimeMillis > Constant.PROTECTED_TIMEOUT) {// 任务执行超时
                        Task originTask = JSON.parseObject(JSON.toJSONString(task), Task.class);

                        if (status.getRetry() < ConfigManager.getQueueRetry()) {
                            // 重新放入任务队列
                            // 更新状态标记为retry
                            status.setState(Constant.RETRY);
                            // 更新重试次数retry + 1
                            status.setRetry(status.getRetry() + 1);
                            task.setTaskStatus(status);
                            // 放入任务队列的队首，优先处理
                            TaskQueue taskQueue = TaskQueueManager.getTaskQueue(task.getQueue());
                            taskQueue.pushTaskToHeader(task);
                        } else {
                            // TODO 持久化到数据库
                        }
                        // 删除备份队列中的该任务
                        backupQueue.finishTask(originTask);
                    }
                    // 继续从备份队列中取出任务，进入下一次循环
                    task = backupQueue.popTask();
                }
            }

        } catch (Throwable e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

    }
}
