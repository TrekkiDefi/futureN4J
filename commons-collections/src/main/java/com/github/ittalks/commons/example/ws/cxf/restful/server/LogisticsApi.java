package com.github.ittalks.commons.example.ws.cxf.restful.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
@Service
public class LogisticsApi implements ILogisticsApi {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String doGet(String firstName, String lastName) {
        // TODO Auto-generated method stub
        log.debug("[firstName : " + firstName + ", lastName : " + lastName + "]");
        // to to something ...
        return "hello fnpac!~";
    }

    @Override
    public String itemConfirm(String xml,
                              HttpServletRequest servletRequest,
                              HttpServletResponse servletResponse) {
        // TODO Auto-generated method stub
        // to do something ...
        return "hello fnpac!~";
    }
}
