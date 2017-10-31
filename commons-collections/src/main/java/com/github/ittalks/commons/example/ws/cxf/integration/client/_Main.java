package com.github.ittalks.commons.example.ws.cxf.integration.client;

import com.github.ittalks.commons.example.ws.cxf.integration.server.IComplexUserService;
import com.github.ittalks.commons.example.ws.cxf.integration.server.ComplexUserService;
import com.github.ittalks.commons.example.ws.cxf.integration.model.User;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
public class _Main {

    public static void main(String[] args) {
        /**
         * 方式一：
         */
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ComplexUserService.class);
        factory.setAddress("http://localhost:9090/futureN4J/ws/users");
        IComplexUserService service = (IComplexUserService) factory.create();

        System.out.println("#############Client getUserByName##############");
        User user = service.getUserByName("fnpac|凡派,");
        System.out.println("获取用户信息：" + user);

        user.setAddress("China");
        service.setUser(user);

        /**
         * 方式二：
         */
//        ComplexUserServiceService service = new ComplexUserServiceService();
//        ComplexUserService port = service.getComplexUserServicePort();
//        Client client = ClientProxy.getClient(port);
//        // 设置拦截器
//        client.getOutInterceptors().add(new LoggingOutInterceptor());
//
//        System.out.println("#############Client getUserByName##############");
//        User user = port.getUserByName("fnpac|凡派,");
//        System.out.println("获取用户信息：" + user);
//
//        user.setAddress("China");
//        port.setUser(user);

        /**
         * 方式三：参见ws-cxf-client.xml
         */
    }
}
