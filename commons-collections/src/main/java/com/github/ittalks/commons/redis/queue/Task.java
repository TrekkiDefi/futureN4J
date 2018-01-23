package com.github.ittalks.commons.redis.queue;

import com.github.ittalks.commons.redis.queue.common.Constant;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by 刘春龙 on 2017/3/3.
 * <p>
 * 队列任务
 */
public class Task implements Serializable {

    /**
     * 任务队列名称
     */
    private String queue;

    /**
     * 任务唯一标识，UUID
     */
    private String id;

    /**
     * 任务类型
     */
    private String type;

    /**
     * 任务数据
     */
    private String data;

    /**
     * 任务状态
     */
    private TaskStatus status;

    private Task() {

    }

    public Task(String queue, String type, String data, TaskStatus status) {
        this.queue = queue;
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.data = data;
        this.status = status;
    }

    public String getQueue() {
        return queue;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TaskStatus getTaskStatus() {
        return status;
    }

    public void setTaskStatus(TaskStatus status) {
        this.status = status;
    }

    public static class TaskStatus {
        /**
         * 任务状态state，normal or retry
         */
        private String state;

        /**
         * 任务生成的时间戳，每次重试不会重置
         */
        private long genTimestamp;

        /**
         * 任务执行的时间戳，每次重试时，都会在该任务从任务队列中取出后（开始执行前）重新设置为当前时间
         */
        private long excTimestamp;

        /**
         * 任务超时后重试的次数
         */
        private int retry;

        public TaskStatus() {
            this.state = Constant.NORMAL;
            this.genTimestamp = System.currentTimeMillis();
            this.excTimestamp = 0;
            this.retry = 0;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        /**
         * 获取任务生成的时间戳，每次重试不会重置
         *
         * @return 任务生成的时间戳
         */
        public long getGenTimestamp() {
            return genTimestamp;
        }

        public void setGenTimestamp(long genTimestamp) {
            this.genTimestamp = genTimestamp;
        }

        /**
         * 获取任务执行的时间戳，每次重试时，都会在该任务从任务队列中取出后（开始执行前）重新设置为当前时间
         *
         * @return 任务执行的时间戳
         */
        public long getExcTimestamp() {
            return excTimestamp;
        }

        public void setExcTimestamp(long excTimestamp) {
            this.excTimestamp = excTimestamp;
        }

        public int getRetry() {
            return retry;
        }

        public void setRetry(int retry) {
            this.retry = retry;
        }
    }

    /**
     * 执行任务
     * <p>
     * 任务状态state不变
     */
    public void doTask(Class clazz) {
        // 获取任务所属队列
        TaskQueue taskQueue = TaskQueueManager.getTaskQueue(this.getQueue());
        String queueMode = taskQueue.getMode();
        if (TaskQueueManager.SAFE.equals(queueMode)) {// 安全队列
            try {
                handleTask(clazz);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            // 任务执行完成，删除备份队列的相应任务
            taskQueue.finishTask(this);
        } else {// 普通队列
            handleTask(clazz);
        }
    }

    /**
     * 执行任务
     *
     * @param clazz 任务执行器
     */
    private void handleTask(Class clazz) {
        try {
            TaskHandler handler = (TaskHandler) clazz.newInstance();
            handler.handle(this.data);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
