package com.github.ittalks.commons.webservice;

import com.github.ittalks.commons.webservice.entity.Users;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.Descriptions;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by 刘春龙 on 2017/4/28.
 *
 * jax-rs接口
 */
@Path(value = "/user")
public interface IUserWebServcie {

    @GET
    @Path(value = "/queryUser")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Descriptions({ @Description(value = "查询用户组", target = DocTarget.METHOD),
            @Description(value = "返回用户组", target = DocTarget.RETURN),
            @Description(value = "Request", target = DocTarget.REQUEST),
            @Description(value = "Response", target = DocTarget.RESPONSE),
            @Description(value = "Resource", target = DocTarget.RESOURCE) })
    public Users queryUser();
}
