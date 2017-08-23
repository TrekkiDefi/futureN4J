package com.github.ittalks.commons.sdk.google.calendar.task.handler;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.commons.redis.queue.Task;
import com.github.ittalks.commons.redis.queue.TaskHandler;
import com.github.ittalks.commons.redis.queue.TaskQueue;
import com.github.ittalks.commons.redis.queue.TaskQueueManager;
import com.github.ittalks.commons.sdk.google.calendar.entity.ReceivedCalendarListEntry;
import com.github.ittalks.commons.sdk.google.calendar.entity.SyncCalendarListEntity;
import com.github.ittalks.commons.sdk.google.calendar.entity.SyncEventsEntity;
import com.github.ittalks.commons.sdk.google.calendar.enums.Queue;
import com.github.ittalks.commons.sdk.google.calendar.proxy.CalendarProxy;
import com.github.ittalks.commons.sdk.google.calendar.task.TaskType;
import com.github.ittalks.commons.sdk.google.client.common.Constraints;
import com.github.ittalks.commons.sdk.google.client.extensions.jdo.JdoDataStoreFactoryProxy;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.store.DataStore;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/14.
 */
public class SynCalendarListTaskHandler implements TaskHandler {

    public static final Logger logger = Logger.getLogger(SynCalendarListTaskHandler.class.getName());

    //`同步设置数据存储`中用于保存当前同步令牌的键。
    private static String SYNC_TOKEN_KEY;
    private static final int SYN_PERIOD = 1;//同步一年内的日历数据

    private static DataStore<String> calendarListDataStore;
    private static DataStore<String> syncSettingsDataStore;

    @Override
    public void handle(String data) {
        logger.info("[" + Thread.currentThread().getName() + "]执行[CalendarList同步]任务，任务数据：" + data);
        SyncCalendarListEntity syncCalendarListEntity = JSON.parseObject(data, SyncCalendarListEntity.class);

        if (syncCalendarListEntity == null) {
            return;
        }

        String userid = syncCalendarListEntity.getUserId();

        try {
            List<String> scopes = Constraints.SCOPES;
            SYNC_TOKEN_KEY = "syncToken#" + userid + "#calendarList";
            logger.info("SYNC_TOKEN_KEY" + SYNC_TOKEN_KEY);
            calendarListDataStore = JdoDataStoreFactoryProxy.getFactory().getDataStore("CalendarListStore");
            syncSettingsDataStore = JdoDataStoreFactoryProxy.getFactory().getDataStore("SyncSettings");
            run(syncCalendarListEntity, userid);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 第一次调用是全量同步，后续调用是增量同步
     *
     * @param userid 用户UID
     * @throws IOException
     */
    private void run(SyncCalendarListEntity syncCalendarListEntity, String userid) throws IOException {
        Calendar client = CalendarProxy.buildCalendar(userid);
        //构造{@link Calendar.CalendarList.List}请求，但不要执行它。
        Calendar.CalendarList.List request = client.calendarList().list();

        //加载最后一次执行存储的同步令牌（如果有）。
        String syncToken = syncSettingsDataStore.get(SYNC_TOKEN_KEY);

        if (syncToken == null) {
            logger.info("Performing full sync.");
        } else {
            logger.info("Performing incremental sync.");
            request.setSyncToken(syncToken);
        }

        //检索日历，一次一页。
        String pageToken = null;
        CalendarList feed = null;

        do {
            request.setPageToken(pageToken);
            try {
                feed = request.execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 410) {
                    // 410状态代码，"Gone"表示同步令牌无效。
                    logger.info(e.getMessage());
                    logger.info("Invalid sync token, clearing calendar store and re-syncing.");
                    syncSettingsDataStore.delete(SYNC_TOKEN_KEY);
                    calendarListDataStore.clear();
                    run(syncCalendarListEntity, userid);
                    return;
                } else {
                    throw e;
                }
            }

            List<CalendarListEntry> calendarListEntries = feed.getItems();
            if (calendarListEntries.size() == 0) {
                logger.info("No new calendars to sync.");
            } else {
                for (CalendarListEntry calendarListEntry : calendarListEntries) {
                    syncCalendarListEntry(syncCalendarListEntity, calendarListEntry, userid);
                }
            }

            pageToken = feed.getNextPageToken();
        } while (pageToken != null);

        syncEvents(syncCalendarListEntity, userid);

        syncSettingsDataStore.set(SYNC_TOKEN_KEY, feed.getNextSyncToken());
        logger.info("Sync complete.");
    }

    private void syncCalendarListEntry(SyncCalendarListEntity syncCalendarListEntity, CalendarListEntry calendarListEntry, String userid) {

        /**
         * 将接收到的`日历`放入`数据队列`
         */
        // 1.获取`数据队列`
        TaskQueue datTaskQueue = TaskQueueManager.getTaskQueue(Queue.DT_QUEUE.getName());
        //2.组装`日历`
        ReceivedCalendarListEntry receivedCalendarListEntry = new ReceivedCalendarListEntry();
        receivedCalendarListEntry.setUserId(syncCalendarListEntity.getUserId());
        receivedCalendarListEntry.setCalendarListEntry(calendarListEntry);
        String calendarListEntryData = JSON.toJSONString(receivedCalendarListEntry);

        Task calendarListEntryTask = new Task(datTaskQueue.getName(), TaskType.CALENDARLIST.getType(), calendarListEntryData, new Task.TaskState());
        //3.将`CalendarListEntry`放入`数据队列`
        datTaskQueue.pushTask(calendarListEntryTask);

        logger.info(String.format("将[CalendarListEntry]加入[数据队列]. 队列: %s, 数据: %s", datTaskQueue.getName(), calendarListEntryTask.getData()));
    }

    private void syncEvents(SyncCalendarListEntity syncCalendarListEntity, String userid) {
        if (!syncCalendarListEntity.isOnly()) {
            //拉取用户的`日历列表`
            Calendar client = CalendarProxy.buildCalendar(syncCalendarListEntity.getUserId());
            CalendarList feed = null;
            try {
                feed = client.calendarList().list().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (feed == null) {
                return;
            }

            List<CalendarListEntry> calendarListEntries = feed.getItems();
            for (CalendarListEntry calendarListEntry : calendarListEntries) {

                /**
                 * 向`消息队列`中加入`同步事件消息`
                 */
                //1.获取`消息队列`
                TaskQueue msTaskQueue = TaskQueueManager.getTaskQueue(Queue.MS_QUEUE.getName());
                //2.组装`同步事件消息`
                SyncEventsEntity syncEventsEntity = new SyncEventsEntity();
                syncEventsEntity.setUserId(syncCalendarListEntity.getUserId());
                syncEventsEntity.setCalendarId(calendarListEntry.getId());

                String synEventData = JSON.toJSONString(syncEventsEntity);

                Task synEventTask = new Task(msTaskQueue.getName(), TaskType.SYN_EVENTS.getType(), synEventData, new Task.TaskState());

                //3.将`同步事件消息`放入`消息队列`
                msTaskQueue.pushTask(synEventTask);
                logger.info(String.format("将[同步事件消息]加入[消息队列]. 队列: %s, 消息: %s", msTaskQueue.getName(), synEventTask.getData()));
            }
        }
    }
}
