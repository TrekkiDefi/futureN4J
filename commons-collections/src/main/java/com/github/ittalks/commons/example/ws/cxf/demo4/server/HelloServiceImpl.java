package com.github.ittalks.commons.example.ws.cxf.demo4.server;

import javax.jws.WebService;

/**
 * Created by liuchunlong on 2017/10/31.
 */
@WebService(
        serviceName = "helloService",
        portName = "helloServicePort",
        endpointInterface = "com.github.ittalks.commons.example.ws.cxf.demo4.server.HelloService"
)
public class HelloServiceImpl {

    public String say(String name){

        return "hello, " + name;
    }
}
