package com.github.ittalks.commons.example.webservice.jaxws.customize;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by 刘春龙 on 2017/10/30.
 * <p>
 * 发布ws服务只需要@WebService注解即可, 如果想要更好的可维护性,则可以通过注解来实现
 *
 * @Description 自定义ws服务, jdk1.6版本仅仅支持 soap1.1格式,jdk1.7及以上版本支持 soap1.2格式
 */
@WebService // 默认静态的方式是不能发布ws服务的
        (
                name = "WebService",// 服务实现的名称
                serviceName = "MyWebServiceService",// 服务的名称，默认在发布的服务实现的名称后面添加Service
                portName = "MyWebServicePort",// 服务类型的名称，默认在发布的服务实现的名称后面添加Port
                targetNamespace = "ws.app.client"// 发布ws服务的命名空间，此空间默认为当前服务包路径的"倒写"，此名称也是 wsimport 命令生成java类时默认的包路径 -p
        )
public class _Main {

    @WebMethod(exclude = true)  // 默认public方法可以发布为ws服务, 如果要排除则配置  exclude=true
    public String helloExc(String name) {
        return name + " hello！";
    }

    // 可以指定wsdl中的方法名，参数名和返回值
    @WebMethod(operationName = "sayHello")
    public
    @WebResult(name = "result")
    String hello(@WebParam(name = "name") String name, @WebParam(name = "age") int age) {
        return name + ",你好!年龄为:" + age;
    }

    public static void main(String[] args) {
        String address = "http://127.0.0.1:9999/webservice";
        Endpoint.publish(address, new _Main());
        System.out.println("访问WSDL的地址为：" + address + "?WSDL");
    }
}
