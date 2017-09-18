package com.github.ittalks.commons.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
     * <p>
     * 启动有序关闭，其中先前提交的任务将被执行，但不会接受任何新任务。如果已经关闭，调用没有额外的作用。
     * <p>
     * 此方法不等待先前提交的任务完成执行。使用{@link #awaitTermination}来做到这一点。
     * <p>
     * 抛出：SecurityException - 如果安全管理器存在，关闭{@link ExecutorService}可能会操作调用者不允许修改的线程，因为它不拥有{@code RuntimePermission("modifyThread")}，或安全管理器的{@code checkAccess}方法拒绝访问。
     */
    public void shutdown() throws SecurityException {
        executor.shutdown();
    }

    /**
     * Blocks 直到所有任务在{@link #shutdown}请求后执行完成，或发生超时，或当前线程中断
     *
     * @param timeout 等待的最长时间
     * @param unit    超时参数的时间单位
     * @return 如果此executor终止，则为true，如果在终止之前已超时，则为false
     * @throws InterruptedException
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
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
