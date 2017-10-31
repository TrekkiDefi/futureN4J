package com.github.ittalks.commons.example.ws.cxf.demo3.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public class _Main {

    public static void main(String[] args) {
        // 获取ws服务名称（获取一个ws服务）
        HelloWorldImplService service = new HelloWorldImplService();

        // 获取服务的类型，有get post soap1.1 soap1.2 jdk1.7及以上才支持soap1.2
        HelloWorldImpl port = service.getHelloWorldImplPort();

        Client client = ClientProxy.getClient(port);
        // 设置拦截器
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        client.getOutInterceptors().add(new AddHeaderInterceptor("admin", "123"));

        // 调用服务提供的方法
        System.out.println(port.sayHello("fnpac"));
    }
}
