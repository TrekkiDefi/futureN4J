package com.github.ittalks.commons.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by 刘春龙 on 2017/5/16.
 * <p>
 * 线程池初始化
 */
public class ExecutorProcessPool {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorProcessPool.class);

    private static ExecutorProcessPool pool = new ExecutorProcessPool();

    private ExecutorService executor;
    private final int threadMax = 10;//最大线程数
    private final String threadName = "worker thread";//线程名称

    private ExecutorProcessPool() {
        logger.info("[Thread Pool Init] - MaximumPoolSize:" + threadMax);
        executor = ExecutorServiceFactory.getInstance().createFixedThreadPool(threadMax, threadName);
    }

    public static ExecutorProcessPool getInstance() {
        return pool;
    }

    /**
     * 关闭线程池，这里要说明的是：调用关闭线程池方法后，线程池会执行完队列中的所有任务才退出
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     *
     * @param task 线程任务
     * @return
     */
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     *
     * @param task 线程任务
     * @return
     */
    public Future<?> submit(Callable<?> task) {
        return executor.submit(task);
    }

    /**
     * 直接提交任务到线程池，无返回值
     * @param task 线程任务
     */
    public void execute(Runnable task){
        executor.execute(task);
    }
}
