package com.github.ittalks.commons.example.ws.cxf.restful.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
@Path("/logisticsApi")
public class LogisticsApiImpl implements ILogisticsApi {

    private Logger log = LoggerFactory.getLogger(getClass());

    @GET
    @Path("/doGet/{first}/{last}")
    @Produces(MediaType.APPLICATION_XML)
    @Override
    public String doGet(@PathParam(value = "first") String firstName, @PathParam(value = "last") String lastName) {
        // TODO Auto-generated method stub
        log.debug("[firstName : " + firstName + ", lastName : " + lastName + "]");
        // to to something ...
        return "hello fnpac!~";
    }

    @POST
    @Path("/itemConfirm")
    @Produces(MediaType.APPLICATION_XML)
    @Override
    public String itemConfirm(String xml,
                              @Context HttpServletRequest servletRequest,
                              @Context HttpServletResponse servletResponse) {
        // TODO Auto-generated method stub
        // to do something ...
        return "hello fnpac!~";
    }
}
