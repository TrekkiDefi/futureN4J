package com.github.ittalks.commons.redis.queue;

import com.github.ittalks.commons.redis.queue.config.ConfigManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/3.
 */
@Component
public class TaskQueueManager implements ApplicationListener<ContextRefreshedEvent> {
    public static final Logger logger = Logger.getLogger(TaskQueueManager.class.getName());

    /**
     * 队列模式：SIMPLE - 简单队列，SAFE - 安全队列
     */
    public static final String SIMPLE = "SIMPLE";
    public static final String SAFE = "SAFE";
    public static String BACK_UP_QUEUE;//备份队列名称
    public static String BACK_UP_QUEUE_BASE = "BACK_UP_QUEUE#";//备份队列名称前缀

    private static Map<String, Object> queueMap =
            new ConcurrentHashMap<>();

    private void initQueueMap() {

        //构造`备份队列名称`
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            BASE64Encoder base64Encoder = new BASE64Encoder();

            // 获取队列名称
            StringBuilder queueNameMulti = new StringBuilder();

            String[] qInfos = ConfigManager.getQueues().trim().split(";");
            for (String qInfo : qInfos) {
                String[] qnms = qInfo.trim().split(":");
                String qName = qnms[0].trim();
                queueNameMulti.append(qName);
            }

            BACK_UP_QUEUE = BACK_UP_QUEUE_BASE + base64Encoder.encode(digest.digest(queueNameMulti.toString().getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }



        logger.info("初始化任务队列...");
        boolean hasSQ = false;

        String[] qInfos = ConfigManager.getQueues().trim().split(";");
        for (String qInfo : qInfos) {
            String[] qnms = qInfo.trim().split(":");
            String qName = qnms[0].trim();
            String qMode = qnms[1].trim();

            if (!StringUtils.isEmpty(qMode) && (qMode.equals(SIMPLE) || qMode.equals(SAFE))) {
                if (!StringUtils.isEmpty(qName)) {
                    if (!queueMap.containsKey(qName)) {
                        if (qMode.equals(SAFE)) {
                            hasSQ = true;//标记存在安全队列
                        }
                        queueMap.put(qName, new RedisTaskQueue(qName, qMode));
                        logger.info("建立队列：" + qName);
                    } else {
                        logger.info("当前队列已存在，请勿重复创建，队列名称：" + qName);
                    }
                } else {
                    logger.info("当前队列名称为空！");
                }
            } else {
                logger.info("当前队列模式不合法，队列名称：" + qName);
            }
        }
        //添加备份队列
        if (hasSQ) {
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
     * @param name  队列名称
     * @return 备份队列
     */
    public static BackupQueue getBackupQueue(String name) {
        Object queue = queueMap.get(name);
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
