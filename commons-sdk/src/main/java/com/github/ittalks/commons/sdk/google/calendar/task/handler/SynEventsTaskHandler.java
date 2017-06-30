package com.github.ittalks.commons.sdk.google.calendar.task.handler;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.queue.Task;
import com.github.ittalks.commons.redis.queue.TaskHandler;
import com.github.ittalks.commons.redis.queue.TaskQueue;
import com.github.ittalks.commons.redis.queue.TaskQueueManager;
import com.github.ittalks.commons.sdk.google.calendar.entity.ReceivedEvent;
import com.github.ittalks.commons.sdk.google.calendar.entity.SyncEventsEntity;
import com.github.ittalks.commons.sdk.google.calendar.enums.Queue;
import com.github.ittalks.commons.sdk.google.calendar.proxy.CalendarProxy;
import com.github.ittalks.commons.sdk.google.calendar.utils.Utils;
import com.github.ittalks.commons.sdk.google.calendar.task.TaskType;
import com.github.ittalks.commons.sdk.google.client.common.Constraints;
import com.github.ittalks.commons.sdk.google.client.extensions.jdo.JdoDataStoreFactoryProxy;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStore;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/6.
 */
public class SynEventsTaskHandler implements TaskHandler {

    public static final Logger logger = Logger.getLogger(SynEventsTaskHandler.class.getName());

    //同步设置数据存储中用于保存当前同步令牌的键。
    private static String SYNC_TOKEN_KEY;
    private static final int SYN_PERIOD = 1;//同步一年内的日历数据

    private static DataStore<String> eventDataStore;
    private static DataStore<String> syncSettingsDataStore;

    @Override
    public void handle(String data) {
        logger.info("执行[事件同步]任务，任务数据：" + data);
        SyncEventsEntity syncEventsEntity = JSON.parseObject(data, SyncEventsEntity.class);

        String userid = syncEventsEntity.getUserId();
        String calendarid = syncEventsEntity.getCalendarId();

        try {
            List<String> scopes = Constraints.SCOPES;
            SYNC_TOKEN_KEY = "syncToken#" + userid  + "#" + calendarid;
            logger.info("SYNC_TOKEN_KEY" + SYNC_TOKEN_KEY);
            eventDataStore = JdoDataStoreFactoryProxy.getFactory().getDataStore("EventStore");
            syncSettingsDataStore = JdoDataStoreFactoryProxy.getFactory().getDataStore("SyncSettings");
            run(userid, calendarid);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 第一次调用是全量同步，后续调用是增量同步
     *
     * @throws IOException
     */
    private void run(String userid, String calendarid) throws IOException {

        Calendar client = CalendarProxy.buildCalendar(userid);
        //构造{@link Calendar.Events.List}请求，但不要执行它。
        Calendar.Events.List request = client.events().list(calendarid == null ? "primary" : calendarid);

        //加载最后一次执行存储的同步令牌（如果有）。
        String syncToken = syncSettingsDataStore.get(SYNC_TOKEN_KEY);

        if (syncToken == null) {
            logger.info("Performing full sync.");
            //设置要在全量同步期间使用的过滤器。 Sync tokens与大多数过滤器不兼容，但您可能希望将全量同步仅限于某个日期范围。
            // 这里我们只同步一年前的Event。
            Date oneYearAgo = Utils.getRelativeDate(java.util.Calendar.YEAR, -1);
            request.setTimeMin(new DateTime(oneYearAgo, TimeZone.getTimeZone("UTC")));
        } else {
            logger.info("Performing incremental sync.");
            request.setSyncToken(syncToken);
        }

        //检索事件，一次一页。
        String pageToken = null;
        Events events = null;

        do {
            request.setPageToken(pageToken);
            try {
                events = request.execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 410) {
                    // 410状态代码，"Gone"表示同步令牌无效。
                    logger.info(e.getMessage());
                    logger.info("Invalid sync token, clearing event store and re-syncing.");
                    syncSettingsDataStore.delete(SYNC_TOKEN_KEY);
                    eventDataStore.clear();
                    run(userid, calendarid);
                    return;
                } else {
                    throw e;
                }
            }

            List<Event> items = events.getItems();
            if (items.size() == 0) {
                logger.info("No new events to sync.");
            } else {
                for (Event event : items) {
                    syncEvent(event, userid, calendarid);
                }
            }

            pageToken = events.getNextPageToken();
        } while (pageToken != null);

        syncSettingsDataStore.set(SYNC_TOKEN_KEY, events.getNextSyncToken());
        logger.info("Sync complete.");
    }

    private void syncEvent(Event event, String userid, String calendarid) throws IOException {
        if ("cancelled".equals(event.getStatus()) && eventDataStore.containsKey(event.getId())) {
//            eventDataStore.delete(event.getId());
            logger.info(String.format("Deleting event: ID=%s", event.getId()));
        } else {
            //获得接收到的`事件`
            logger.info(String.format("Syncing event: ID=%s, Name=%s", event.getId(), event.getSummary()));
            //将接收到的`事件`放入`数据队列`
            //1.获取`数据队列`
            TaskQueue taskQueue = TaskQueueManager.getTaskQueue(Queue.DAT_QUEUE.getName());
            //2.组装`事件`
            ReceivedEvent receivedEvent = new ReceivedEvent();
            receivedEvent.setUserId(userid);
            receivedEvent.setCalendarId(calendarid == null ? "primary" : calendarid);
            receivedEvent.setEvent(event);

            String eventData = JSON.toJSONString(receivedEvent);
            Task task = new Task(taskQueue.getName(), TaskType.EVENT.getType(), eventData, new Task.TaskState());
            //3.将`事件`放入`数据队列`
            taskQueue.pushTask(task);
            logger.info(String.format("将[事件]加入[数据队列]. 队列: %s, 数据: %s", taskQueue.getName(), task.getData()));
//            eventDataStore.set(event.getId(), event.toString());
        }
    }
}
