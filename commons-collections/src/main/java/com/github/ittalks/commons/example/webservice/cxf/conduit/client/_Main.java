package com.github.ittalks.commons.example.webservice.cxf.conduit.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public class _Main {

    public static void main(String[] args) {
        // 获取ws服务名称（获取一个ws服务）
        HelloService_Service service = new HelloService_Service();

        //获取服务的类型，有get post soap1.1 soap1.2 jdk1.7及以上才支持soap1.2
        HelloService port = service.getHelloServicePort();

        Client client = ClientProxy.getClient(port);
        HTTPConduit http = (HTTPConduit) client.getConduit();

        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(36000);
        httpClientPolicy.setAllowChunking(false);
        httpClientPolicy.setReceiveTimeout(32000);
        http.setClient(httpClientPolicy);

        //调用服务提供的方法
        System.out.println(port.say("fnpac"));
    }
}
