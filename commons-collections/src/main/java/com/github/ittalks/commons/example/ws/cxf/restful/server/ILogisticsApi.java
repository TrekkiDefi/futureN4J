package com.github.ittalks.commons.example.ws.cxf.restful.server;

import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
@Path("/logisticsApi")
public interface ILogisticsApi {

    @GET
    @Path("/doGet/{first}/{last}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    String doGet(@PathParam(value = "first") String firstName, @PathParam(value = "last") String lastName);

    /**
     * 访问：http://127.0.0.1:9090/futureN4J/ws/rest?_wadl<br/>
     * 即可查看@{@link Descriptions}标注的wadl xml文件
     *
     * @param xml
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @POST
    @Path("/itemConfirm")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Descriptions({@Description(value = "item Confirm方法", target = DocTarget.METHOD),
            @Description(value = "返回值", target = DocTarget.RETURN),
            @Description(value = "请求", target = DocTarget.REQUEST),
            @Description(value = "响应", target = DocTarget.RESPONSE),
            @Description(value = "资源", target = DocTarget.RESOURCE)})
    String itemConfirm(String xml,
                       @Context HttpServletRequest servletRequest,
                       @Context HttpServletResponse servletResponse);
}
