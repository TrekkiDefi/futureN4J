package com.github.ittalks.commons.sdk.google.calendar.task;

import java.util.concurrent.TimeUnit;

/**
 * Created by 刘春龙 on 2017/4/13.
 */
public class RetryPolicy {

    private static final long internal = 60L;// 重试间隔：60s

    public static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(internal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
