package com.github.ittalks.fn;

import com.github.ittalks.fn.common.task.AsyncTask;
import com.github.ittalks.fn.config.RootApplicationConfig;
import com.github.ittalks.fn.config.WebApplicationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by 刘春龙 on 2017/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {
        RootApplicationConfig.class,
        WebApplicationConfig.class
})
public class FnAppTest {

    protected static final Logger logger = LoggerFactory.getLogger(FnAppTest.class);

    @Autowired
    private AsyncTask asyncTask;

    @Test
    public void asyncTaskTest() throws InterruptedException, ExecutionException {
        Future<String> task1 = asyncTask.doTask1();
        Future<String> task2 = asyncTask.doTask2();

        while(true) {
            if(task1.isDone() && task2.isDone()) {
                logger.info("Task1 result: {}", task1.get());
                logger.info("Task2 result: {}", task2.get());
                break;
            }
            Thread.sleep(1000);
        }

        logger.info("All tasks finished.");
    }
}
