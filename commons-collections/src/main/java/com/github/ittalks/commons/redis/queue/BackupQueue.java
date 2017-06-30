package com.github.ittalks.commons.redis.queue;

/**
 * Created by 刘春龙 on 2017/3/5.
 * <p>
 * 安全队列对应的备份队列
 */
public abstract class BackupQueue {

    /**
     * 获取队列名<br>
     * 同时支持多个队列，每个队列都应该有个名字。
     *
     * @return 队列名
     */
    public abstract String getName();

    /**
     * 从队尾取一个任务，然后在将其放入队首
     *
     * @return 任务
     */
    public abstract Task popTask();

    /**
     * 备份队列的任务完成，即超时任务需要从当前备份队列中删除
     *
     * @param task 超时任务
     */
    public abstract void finishTask(Task task);
}
