package com.github.ittalks.fn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.ittalks.commons.redis.queue.Task;
import com.github.ittalks.commons.redis.queue.TaskQueue;
import com.github.ittalks.commons.redis.queue.TaskQueueManager;
import com.github.ittalks.commons.sdk.google.calendar.enums.Queue;
import com.github.ittalks.commons.sdk.google.calendar.task.TaskType;
import com.github.ittalks.fn.utils.FnAppTest;
import org.junit.Test;

/**
 * Created by 刘春龙 on 2017/8/23.
 */
public class RedisQueueTest extends FnAppTest {

    @Test
    public void pushQueueTest() {
        // 1.获取队列
        TaskQueue taskQueue = TaskQueueManager.getTaskQueue(Queue.MS_QUEUE.getName());
        //2.创建任务
        JSONObject ob = new JSONObject();
        ob.put("data", "Syn CalendarList...");
        String data = JSON.toJSONString(ob);
        Task task = new Task(taskQueue.getName(), TaskType.SYN_CALENDARLIST.getType(), data, new Task.TaskState());
        //3.将任务加入队列
        taskQueue.pushTask(task);
    }
}
