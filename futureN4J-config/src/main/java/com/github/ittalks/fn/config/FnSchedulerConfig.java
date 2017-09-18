package com.github.ittalks.fn.config;

import com.github.ittalks.commons.redis.queue.BackupQueueMonitor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by 刘春龙 on 2017/9/1.
 */
@Configuration
@EnableScheduling
public class FnSchedulerConfig {

    /**
     * 备份队列监控，用于异常中断任务的重试
     */
    @DependsOn("taskQueueManager")
    @Scheduled(initialDelay = 10000, fixedDelay = 1000 * 30)
    public void backupQueueMonitor() {
        BackupQueueMonitor monitor = new BackupQueueMonitor();
        monitor.monitor();
    }
}
