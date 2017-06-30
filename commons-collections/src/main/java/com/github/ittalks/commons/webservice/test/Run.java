package com.github.ittalks.commons.webservice.test;

import com.github.ittalks.commons.webservice.impl.rs.UserWebService;
import com.github.ittalks.commons.webservice.impl.ws.HelloWebService;
import com.github.ittalks.commons.webservice.interceptor.MessageInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 刘春龙 on 2017/5/2.
 *
 * 快捷启动webservice服务
 */
public class Run {

    private static Logger logger = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {
        // jax-ws
        // http://localhost:9000/helloWorld?WSDL
//        logger.info("Server start。。  ");
//        HelloWebService service = new HelloWebService();
//        String address = "http://localhost:9000/helloWorld";
//        Endpoint.publish(address, service);
//        logger.info("JAX_WS服务启动成功。。 ");
        // 使用jaxWs对其进行发布
        JaxWsServerFactoryBean wsBean = new JaxWsServerFactoryBean();
        wsBean.setServiceBean(new HelloWebService());
        wsBean.setServiceClass(HelloWebService.class);
        wsBean.getInInterceptors().add(new MessageInterceptor(Phase.RECEIVE));
        wsBean.getOutInterceptors().add(new MessageInterceptor(Phase.SEND));
        wsBean.setAddress("http://localhost:9090/helloWorld");
        wsBean.create(); // 内部使用jetty服务器做为支持
        logger.info("JAX_WS服务启动成功。。 ");

        // jax-rs
        JAXRSServerFactoryBean jaxRsbean = new JAXRSServerFactoryBean();
        jaxRsbean.setServiceBean(new UserWebService());// 加载服务类
        jaxRsbean.setAddress("http://localhost:9091");// 声明地址，注意只声明地址和端口即可
        jaxRsbean.getInInterceptors().add(new MessageInterceptor(Phase.RECEIVE));
        jaxRsbean.getOutInterceptors().add(new MessageInterceptor(Phase.SEND));
        jaxRsbean.create();// 启动
        logger.info("JAX-RS服务启动成功。。 ");
    }
}
