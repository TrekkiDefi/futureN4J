package com.github.ittalks.commons.redis.queue;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.RedisManager;
import com.github.ittalks.commons.redis.queue.common.Constant;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/3.
 * <p>
 * 任务队列Redis实现<br/>
 * 采用每次获取Jedis并放回Pool的方式<br/>
 */
public class RedisTaskQueue extends TaskQueue {

    private static final Logger logger = Logger.getLogger(RedisTaskQueue.class.getName());

    private static final int REDIS_DB_IDX = 0;
    private final String name;
    private final String mode;

    /**
     * 构造函数
     *
     * @param name 任务队列名称
     * @param mode 队列模式
     */
    public RedisTaskQueue(String name, String mode) {
        if (mode == null || "".equals(mode)) {
            mode = TaskQueueManager.DEFAULT;
        }
        this.name = name;
        this.mode = mode;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getMode() {
        return this.mode;
    }

    @Override
    public void pushTask(Task task) {
        Jedis jedis = null;
        try {
            jedis = RedisManager.getResource(REDIS_DB_IDX);
            String taskJson = JSON.toJSONString(task);
            jedis.lpush(this.name, taskJson);
        } catch (Throwable e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisManager.returnResource(jedis);
            }
        }
    }

    @Override
    public void pushTaskToHeader(Task task) {

        Jedis jedis = null;
        try {
            jedis = RedisManager.getResource(REDIS_DB_IDX);
            String taskJson = JSON.toJSONString(task);
            jedis.rpush(this.name, taskJson);
        } catch (Throwable e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisManager.returnResource(jedis);
            }
        }

    }


    @Override
    public Task popTask() {
        Jedis jedis = null;
        Task task = null;
        try {
            jedis = RedisManager.getResource(REDIS_DB_IDX);

            // 判断队列模式
            if (TaskQueueManager.SAFE.equals(getMode())) {// 安全队列
                /**
                 * 1.采用阻塞队列，获取任务队列中的任务(brpop)；
                 * 2.判断任务是否超时；
                 * 3.更新任务的执行时间戳，放入备份队列的队首；
                 *
                 * 任务状态不变，默认值为`normal`
                 */
                // 1.采用阻塞队列，获取任务队列中的任务(brpop)；
                List<String> result = jedis.brpop(0, getName());
                task = JSON.parseObject(result.get(1), Task.class);

                // 2.判断任务是否超时；
                Task.TaskStatus status = task.getTaskStatus();// 获取任务状态

                long taskGenTimeMillis = status.getGenTimestamp();// 任务生成的时间戳
                long currentTimeMillis = System.currentTimeMillis();// 当前时间戳
                long intervalTimeMillis = currentTimeMillis - taskGenTimeMillis;// 任务的存活时间
                if (intervalTimeMillis <= Constant.ALIVE_TIMEOUT) {// 如果大于存活超时时间，则不再执行
                    // 3.更新任务的执行时间戳，放入备份队列的队首；
                    task.getTaskStatus().setExcTimestamp(System.currentTimeMillis());// 更新任务的执行时间戳
                    jedis.lpush(TaskQueueManager.BACK_UP_QUEUE, JSON.toJSONString(task));
                }
            } else if (TaskQueueManager.DEFAULT.equals(getMode())) {// 简单队列

                List<String> result = jedis.brpop(0, getName());
                String taskJson = result.get(1);
                task = JSON.parseObject(taskJson, Task.class);
            }
        } catch (Throwable e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisManager.returnResource(jedis);
            }
        }
        return task;
    }

    @Override
    protected void finishTask(Task task) {
        if (TaskQueueManager.SAFE.equals(getMode())) {
            // 安全队列，删除备份队列中的任务
            Jedis jedis = null;
            try {
                jedis = RedisManager.getResource(REDIS_DB_IDX);
                String taskJson = JSON.toJSONString(task);
                jedis.lrem(TaskQueueManager.BACK_UP_QUEUE, 0, taskJson);
            } catch (Throwable e) {
                logger.info(e.getMessage());
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    RedisManager.returnResource(jedis);
                }
            }
        }
    }

}
