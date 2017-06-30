package com.github.ittalks.commons.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 刘春龙 on 2017/5/16.
 * <p>
 * 线程池构造工厂
 */
public class ExecutorServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceFactory.class);

    private static final ExecutorServiceFactory executorFactory = new ExecutorServiceFactory();

    /**
     * 构造方法私有化
     */
    private ExecutorServiceFactory() {
    }

    /**
     * 获取ExecutorServiceFactory
     *
     * @return
     */
    public static ExecutorServiceFactory getInstance() {
        return executorFactory;
    }

    /**
     * 创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。
     *
     * @param threadName 线程名称
     * @return
     */
    public ExecutorService createScheduledThreadPool(String threadName) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();// CPU个数
        ExecutorService executors = Executors.newScheduledThreadPool(availableProcessors * 10, getThreadFactory(threadName));
        return executors;
    }

    /**
     * 创建一个使用单个 "worker" 线程的Executor，以无界队列方式来运行该线程。
     * <p>
     * 注意，如果因为在关闭前的执行期间出现失败而终止了此单个线程，那么如果需要，一个新线程将代替它执行后续的任务。
     * <p>
     * 可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。
     * 与其他等效的 newFixedThreadPool(1) 不同，可保证无需重新配置此方法所返回的执行程序即可使用其他的线程。
     *
     * @param threadName 线程名称
     * @return
     */
    public ExecutorService createSingleThreadExecutor(String threadName) {
        ExecutorService executors = Executors.newSingleThreadExecutor(getThreadFactory(threadName));
        return executors;
    }

    /**
     * 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。对于执行很多短期异步任务的程序而言，这些线程池通常可提高程序性能。
     * <p>
     * 调用 "execute" 将重用以前构造的线程（如果线程可用）。如果现有线程没有可用的，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程。
     * <p>
     * 因此，长时间保持空闲的线程池不会使用任何资源。注意，可以使用 ThreadPoolExecutor 构造方法创建具有类似属性但细节不同（例如超时参数）的线程池。
     *
     * @param threadName 线程名称
     * @return
     */
    public ExecutorService createCachedThreadPool(String threadName) {
        ExecutorService executors = Executors.newCachedThreadPool(getThreadFactory(threadName));
        return executors;
    }

    /**
     * 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
     * <p>
     * 在任意点，在大多数 nThreads 线程会处于处理任务的活动状态。
     * <p>
     * 如果在所有线程处于活动状态时提交附加任务，则在有可用线程之前，附加任务将在队列中等待。
     * <p>
     * 如果在关闭前的执行期间由于失败而导致任何线程终止，那么一个新线程将代替它执行后续的任务（如果需要）。在某个线程被显式地关闭之前，池中的线程将一直存在。
     *
     * @param count      初始化线程池线程数量
     * @param threadName 线程名称
     * @return
     */
    public ExecutorService createFixedThreadPool(int count, String threadName) {
        ExecutorService executors = Executors.newFixedThreadPool(count, getThreadFactory(threadName));
        return executors;
    }

    /**
     * 创建一个可根据需要创建新线程的线程池,有线程监控.
     *
     * @param threadName 线程名称
     * @param isDebug    是否打印debug日志
     * @return
     */
    public ExecutorService createMonitorCachedThreadPool(String threadName, boolean isDebug) {
        ExecutorService executors = new MonitorThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                getThreadFactory(threadName), isDebug);

        return executors;
    }

    public ExecutorService createMonitorCachedThreadPool(String threadName) {
        return createMonitorCachedThreadPool(threadName, true);
    }

    public static class MonitorThreadPoolExecutor extends ThreadPoolExecutor {

        private boolean isDebug = false;//是否打印debug日志

        /**
         * Creates a new {@code ThreadPoolExecutor} with the given initial
         * parameters and default rejected execution handler.
         *
         * @param corePoolSize    the number of threads to keep in the pool, even
         *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
         * @param maximumPoolSize the maximum number of threads to allow in the
         *                        pool
         * @param keepAliveTime   when the number of threads is greater than
         *                        the core, this is the maximum time that excess idle threads
         *                        will wait for new tasks before terminating.
         * @param unit            the time unit for the {@code keepAliveTime} argument
         * @param workQueue       the queue to use for holding tasks before they are
         *                        executed.  This queue will hold only the {@code Runnable}
         *                        tasks submitted by the {@code execute} method.
         * @param threadFactory   the factory to use when the executor
         *                        creates a new thread
         * @throws IllegalArgumentException if one of the following holds:<br>
         *                                  {@code corePoolSize < 0}<br>
         *                                  {@code keepAliveTime < 0}<br>
         *                                  {@code maximumPoolSize <= 0}<br>
         *                                  {@code maximumPoolSize < corePoolSize}
         * @throws NullPointerException     if {@code workQueue}
         *                                  or {@code threadFactory} is null
         */
        public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, boolean isDebug) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
            this.isDebug = isDebug;
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            if (isDebug) {
                logger.info("Thread[ " + r + "] Execute Start, Thread Pool Status -  [CorePoolSize: " + this.getCorePoolSize() + "； PoolSize: " + this.getPoolSize() + "； TaskCount: " + this.getTaskCount() + "； CompletedTaskCount: "
                        + this.getCompletedTaskCount() + "； LargestPoolSize: " + this.getLargestPoolSize() + "； ActiveCount: " + this.getActiveCount() + "]");
            }
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            if (isDebug) {
                if (t != null) {
                    logger.error("Thread[ " + r + "] Execute End，Throw Exception:" + t + ", Thread Pool Status -  [CorePoolSize: " + this.getCorePoolSize() + "； PoolSize: " + this.getPoolSize() + "； TaskCount: " + this.getTaskCount() + "； CompletedTaskCount: "
                            + this.getCompletedTaskCount() + "； LargestPoolSize: " + this.getLargestPoolSize() + "； ActiveCount: " + this.getActiveCount() + "]");

                }
            }
        }

        @Override
        protected void terminated() {
            super.terminated();
        }
    }

    /**
     * 获取线程工厂
     *
     * @param threadName 线程名称
     * @return
     */
    private ThreadFactory getThreadFactory(final String threadName) {
        return new ThreadFactory() {
            AtomicInteger index = new AtomicInteger();

            /**
             * Constructs a new {@code Thread}.  Implementations may also initialize
             * priority, name, daemon status, {@code ThreadGroup}, etc.
             *
             * @param r a runnable to be executed by new thread instance
             * @return constructed thread, or {@code null} if the request to
             * create a thread is rejected
             */
            @Override
            public Thread newThread(Runnable r) {
                SecurityManager securityManager = System.getSecurityManager();
                ThreadGroup threadGroup = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
                Thread thread = new Thread(threadGroup, r);
                thread.setName("[" + (threadName == null ? "worker thread" : threadName) + "] - " + index.incrementAndGet());
                return thread;
            }
        };
    }
}
