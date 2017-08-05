package com.github.ittalks.fn;

import com.github.ittalks.fn.common.task.AsyncTask;
import com.github.ittalks.fn.utils.FnAppTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by 刘春龙 on 2017/8/5.
 */
public class AsyncTest extends FnAppTest {
    @Autowired
    private AsyncTask asyncTask;

    //    18:45:35.563 [AsyncExecutor-1] INFO  c.g.ittalks.fn.common.task.AsyncTask - Task1 started.
    //    18:45:35.565 [PolicyExecutor-1] INFO  c.g.ittalks.fn.common.task.AsyncTask - Task2 started.
    //    18:45:38.565 [PolicyExecutor-1] INFO  c.g.ittalks.fn.common.task.AsyncTask - Task2 finished, time elapsed: 3000 ms.
    //    18:45:40.564 [AsyncExecutor-1] INFO  c.g.ittalks.fn.common.task.AsyncTask - Task1 finished, time elapsed: 5000 ms.
    @Test
    public void asyncTaskTest() throws InterruptedException, ExecutionException {
        Future<String> task1 = asyncTask.doTask1();
        Future<String> task2 = asyncTask.doTask2();

        while (true) {
            if (task1.isDone() && task2.isDone()) {
                logger.info("Task1 result: {}", task1.get());
                logger.info("Task2 result: {}", task2.get());
                break;
            }
            Thread.sleep(1000);
        }

        logger.info("All tasks finished.");
    }
}
