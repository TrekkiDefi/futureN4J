package com.github.ittalks.commons.example.webservice.cxf.conduit.server;

import javax.jws.WebService;

/**
 * Created by liuchunlong on 2017/10/31.
 */

/**
 * 如果不加该注解，会报错：
 * javax.xml.ws.WebServiceException: Service endpoint interface does not have a @WebService annotation.
 */
@WebService
public interface HelloService {

    String say(String name);
}
