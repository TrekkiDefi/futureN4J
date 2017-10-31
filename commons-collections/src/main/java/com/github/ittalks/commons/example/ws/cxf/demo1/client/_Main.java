package com.github.ittalks.commons.example.ws.cxf.demo1.client;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public class _Main {

    public static void main(String[] args) {
        // 获取ws服务名称（获取一个ws服务）
        HelloWorldImplService service = new HelloWorldImplService();

        //获取服务的类型，有get post soap1.1 soap1.2 jdk1.7及以上才支持soap1.2
        HelloWorldImpl port = service.getHelloWorldImplPort();

        //调用服务提供的方法
        System.out.println(port.sayHello("fnpac"));
    }
}
