package com.github.ittalks.commons.redis.queue;

import com.github.ittalks.commons.redis.queue.config.ConfigManager;
import com.github.ittalks.fn.common.advice.exception.NestedException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/3.
 */
@Component
public class TaskQueueManager implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = Logger.getLogger(TaskQueueManager.class.getName());

    /**
     * 队列模式：DEFAULT - 简单队列，SAFE - 安全队列
     */
    public static final String DEFAULT = "default";
    public static final String SAFE = "safe";
    public static String BACK_UP_QUEUE;//备份队列名称
    public static String BACK_UP_QUEUE_BASE = "back_up_queue_";//备份队列名称前缀

    private static Map<String, Object> queueMap =
            new ConcurrentHashMap<>();

    private void initQueueMap() {

        // 构造`备份队列名称`
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            BASE64Encoder base64Encoder = new BASE64Encoder();

            // 获取队列名称
            StringBuilder queueNameMulti = new StringBuilder();

            String[] qInfos = ConfigManager.getQueues().trim().split(";");
            for (String qInfo : qInfos) {
                String qName = qInfo.trim().split(":")[0].trim();
                queueNameMulti.append(qName);
            }

            BACK_UP_QUEUE = BACK_UP_QUEUE_BASE + base64Encoder.encode(digest.digest(queueNameMulti.toString().getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }



        logger.info("初始化任务队列...");
        boolean hasSq = false;

        String[] qInfos = ConfigManager.getQueues().trim().split(";");
        for (String qInfo : qInfos) {
            String qName = qInfo.trim().split(":")[0].trim();
            String qMode = qInfo.trim().split(":")[1].trim();

            if(!"".equals(qMode) && !qMode.equals(DEFAULT) && !qMode.equals(SAFE)) {
                throw new NestedException("The current queue mode is invalid, the queue name：" + qName);
            }

            if(!"".equals(qName)) {
                if(!queueMap.containsKey(qName)) {
                    if (qMode.equals(SAFE)) {
                        hasSq = true;// 标记存在安全队列
                    }
                    queueMap.put(qName, new RedisTaskQueue(qName, qMode));
                    logger.info("Creating a task queue：" + qName);
                } else {
                    logger.info("The current queue already exists. Do not create the queue name repeatedly：" + qName);
                }
            } else {
                throw new NestedException("The current queue name is empty!");
            }
        }
        //添加备份队列
        if (hasSq) {
            BackupQueue backupQueue = new RedisBackupQueue(BACK_UP_QUEUE);
            queueMap.put(BACK_UP_QUEUE, backupQueue);
            logger.info("初始化备份队列...");
        }

    }


    /**
     * 根据名称获取任务队列
     * @param name  队列名称
     * @return 任务队列
     */
    public static TaskQueue getTaskQueue(String name) {
        Object queue = queueMap.get(name);
        if (queue != null && queue instanceof TaskQueue) {
            return (TaskQueue)queue;
        }
        return null;
    }

    /**
     * 根据名称获取备份队列
     *
     * @return 备份队列
     */
    public static BackupQueue getBackupQueue() {
        Object queue = queueMap.get(BACK_UP_QUEUE);
        if (queue != null && queue instanceof BackupQueue) {
            return (BackupQueue)queue;
        }
        return null;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            initQueueMap();
        }
    }
}
