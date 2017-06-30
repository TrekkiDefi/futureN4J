package com.github.ittalks.commons.redis.queue;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.RedisManager;
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

    public static final Logger logger = Logger.getLogger(RedisTaskQueue.class.getName());

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

            //判断队列模式
            if (TaskQueueManager.SAFE.equals(getMode())) {
                //安全队列
                /**
                 * 采用阻塞队列，并将当前执行的任务放入备份队列，
                 * 任务状态不变，默认值为`normal`
                 */
                String taskJson = jedis.brpoplpush(getName(), TaskQueueManager.BACK_UP_QUEUE, 0);
                task = JSON.parseObject(taskJson, Task.class);
            } else if (TaskQueueManager.SIMPLE.equals(getMode())) {
                //简单队列
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
            //安全队列，删除备份队列中的任务
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
