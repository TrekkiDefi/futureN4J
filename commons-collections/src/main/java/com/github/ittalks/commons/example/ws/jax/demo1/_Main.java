package com.github.ittalks.commons.example.ws.jax.demo1;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by 刘春龙 on 2017/10/30.
 * <p>
 * 默认静态方法是不能发不成ws服务的
 * <p>
 * 如果指定{@link #hello(String)} 为static，则报如下错误：<br/>
 * com.sun.xml.internal.ws.model.RuntimeModelerException: 由类com.github.ittalks.fn.common.ws.jx.Demo1定义的 Web 服务不包含任何有效的 WebMethods
 *
 * @Description 自定义ws，jdk1.7版本及以上才支持soap1.2
 */
@WebService
public class _Main {

    //提供一个方法，供下面测试用的
    public String hello(String name) {
        return name + " hello！";
    }

    public static void main(String[] args) {
        // 一个端口可以发布多个ws服务，所以后面还有 /+服务名
        String address = "http://127.0.0.1:6666/ws";
//        String address2 = "http://127.0.0.1:6666/ws2";
        // 创建一个服务端点
        Endpoint.publish(address, new _Main());
//        Endpoint.publish(address2, new _ProxyMain());
        System.out.println("访问WSDL的地址为：" + address + "?WSDL");
//        System.out.println("访问WSDL的地址为：" + address2 + "?WSDL");
    }
}
