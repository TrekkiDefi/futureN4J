package com.github.ittalks.fn.web.service;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by 刘春龙 on 2017/2/14.
 * 測試Jersey Restful
 * 測試地址：http://localhost/futureN4J/rest/jersey/tom/say
 */
@Component
@Path("jersey")
public class JerseyService {

    @Path("{username}/say")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello(@PathParam("username") String username) {
        return username + " say hello world.";
    }
}
