package com.github.ittalks.fn.config;

import com.github.ittalks.commons.thread.pool.ExecutorProcessPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by 刘春龙 on 2017/8/29.
 */
@Component
public class ContextClosedListener implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(ContextClosedListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextStoppedEvent || applicationEvent instanceof ContextClosedEvent) {
            try {
                // 关闭线程池
                ExecutorProcessPool executorProcessPool = ExecutorProcessPool.getInstance();
                logger.info("已关闭线程池[" + executorProcessPool.toString() + "]");
                executorProcessPool.shutdown();

                // 等待10s
                executorProcessPool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
