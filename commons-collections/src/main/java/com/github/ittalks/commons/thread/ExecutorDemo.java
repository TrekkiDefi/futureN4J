package com.github.ittalks.commons.thread;

import com.github.ittalks.commons.thread.pool.ExecutorProcessPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by 刘春龙 on 2017/5/16.
 *
 * 线程池使用Demo
 */
public class ExecutorDemo {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorDemo.class);

    public static void main(String[] args) {
        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

        for (int i = 0; i < 200; i++) {
            Future<?> future = pool.submit(new CallableTask(i + ""));
            //如果接收线程返回值，future.get() 会阻塞，如果这样写就是一个线程一个线程执行。所以非特殊情况不建议使用接收返回值的
            try {
                logger.info(future.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 200; i++) {
            pool.execute(new RunnableTask(i+""));
        }

        //关闭线程池，如果是需要长期运行的线程池，不用调用该方法。
        //监听程序退出的时候最好执行一下。
        pool.shutdown();
    }

    /**
     * 实现Callable方式
     */
    static class CallableTask implements Callable {

        private String taskName;

        public CallableTask(String taskName) {
            this.taskName = taskName;
        }
        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public Object call() throws Exception {
            //Java 6/7最佳的休眠方法为TimeUnit.MILLISECONDS.sleep(100);
            //最好不要用 Thread.sleep(100);
            TimeUnit.MILLISECONDS.sleep((long)Math.random() * 1000);

            return taskName;
        }
    }

    /**
     * 实现Runnable方式
     */
    static class RunnableTask implements Runnable {

        private String taskName;

        public RunnableTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {

            //Java 6/7最佳的休眠方法为TimeUnit.MILLISECONDS.sleep(100);
            //最好不要用 Thread.sleep(100);
            try {
                TimeUnit.MILLISECONDS.sleep((long)Math.random() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
