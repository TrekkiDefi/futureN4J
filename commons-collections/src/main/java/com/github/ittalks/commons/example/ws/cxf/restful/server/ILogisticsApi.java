package com.github.ittalks.commons.example.ws.cxf.restful.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
public interface ILogisticsApi {

    String doGet(String firstName, String lastName);
    String itemConfirm(String xmlParam,
                       HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse);
}
