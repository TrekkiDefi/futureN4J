package com.github.ittalks.commons.sdk.google.calendar.task.pool;


import com.github.ittalks.commons.thread.pool.ExecutorServiceFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/4/10.
 *
 * 线程处理类，执行任务
 */
public class ExecutorProcessPool {

    public static final Logger logger = Logger.getLogger(ExecutorProcessPool.class.getName());

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
    public void shutdown(){
        executor.shutdown();
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     * @param task 线程任务
     * @return
     */
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
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
