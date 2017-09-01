package com.github.ittalks.commons.redis.queue;

import com.github.ittalks.commons.redis.queue.common.TS;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by 刘春龙 on 2017/3/3.
 * <p>
 * 队列任务
 */
public class Task implements Serializable {

    private String queue;//任务队列名称
    private String id;//任务唯一标识，UUID
    private String type;//任务类型
    private String data;//任务数据
    private TaskState ts;//任务状态

    public Task() {

    }

    public Task(String queue, String type, String data, TaskState ts) {
        this.queue = queue;
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.data = data;
        this.ts = ts;
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

    public TaskState getTs() {
        return ts;
    }

    public void setTs(TaskState ts) {
        this.ts = ts;
    }

    public static class TaskState {
        private String state;//任务状态
        private long timestamp;//任务时间戳
        private int repeat;//任务超时后当前重复执行的次数

        public TaskState() {
            this.state = TS.NORMAL;
            this.timestamp = System.currentTimeMillis();
            this.repeat = 0;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getRepeat() {
            return repeat;
        }

        public void setRepeat(int repeat) {
            this.repeat = repeat;
        }
    }

    /**
     * 执行任务
     * <p>
     * 任务状态不变
     */
    public void doTask(Class clazz) {
        //获取任务所属队列
        TaskQueue taskQueue = TaskQueueManager.getTaskQueue(getQueue());
        String queueMode = taskQueue.getMode();
        if (TaskQueueManager.SAFE.equals(queueMode)) {
            //安全队列
            //判断任务是否在保护期内
            Task.TaskState ts = getTs();//获取任务状态
            long taskTimeMillis = ts.getTimestamp();//任务的时间戳
            long currentTimeMillis = System.currentTimeMillis();//当前时间戳
            long intervalTimeMillis = currentTimeMillis - taskTimeMillis;//任务在队列中等待的时间，单位：ms
            if (intervalTimeMillis < (TS.TIMEOUT - TS.PROTECTED_TIME)) {
                try {
                    handleTask(clazz);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                //任务执行完成，删除备份队列的相应任务
                taskQueue.finishTask(this);
            }
        } else {
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
