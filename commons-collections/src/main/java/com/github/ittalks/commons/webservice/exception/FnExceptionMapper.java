package com.github.ittalks.commons.webservice.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by 刘春龙 on 2017/4/28.
 *
 * 统一异常处理
 */
public class FnExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
//        Response.ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return Response.status(500).entity(throwable.getMessage()).build();
    }
}
