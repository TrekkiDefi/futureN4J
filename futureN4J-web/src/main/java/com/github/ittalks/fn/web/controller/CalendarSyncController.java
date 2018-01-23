package com.github.ittalks.fn.web.controller;

import com.github.ittalks.commons.sdk.google.calendar.service.CalendarSyncService;
import com.github.ittalks.fn.common.result.APIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/7.
 * <p>
 * 谷歌日历数据同步接口
 */
@Controller
@RequestMapping("/google/calendar/sync")
public class CalendarSyncController {

    private static final Logger logger = Logger.getLogger(CalendarSyncController.class.getName());

    @Autowired
    private CalendarSyncService calendarService;

    /**
     * 谷歌日历数据同步接口
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param userid   用户ID
     * @return 谷歌日历同步结果
     * @throws IOException
     */
    @RequestMapping(value = "/{userid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object sync(HttpServletRequest request, HttpServletResponse response, @PathVariable("userid") String userid) throws IOException {
        //请求参数userid
        logger.info("同步谷歌日历, 用户ID:" + userid);

        //向`消息队列`中加入`同步日历消息`
        calendarService.sync(userid);

        return APIResult.Y();
    }
}
