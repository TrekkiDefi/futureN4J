package com.github.ittalks.commons.example.webservice.cxf.restful.server;

import com.github.ittalks.commons.example.webservice.cxf.integration.server.interceptor.MessageInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 刘春龙 on 2017/11/1.
 */
public class _Main {

    private static Logger logger = LoggerFactory.getLogger(_Main.class);

    public static void main(String[] args) {
        JAXRSServerFactoryBean jaxRsbean = new JAXRSServerFactoryBean();
        jaxRsbean.setServiceClass(LogisticsApi.class);// 加载服务类
        jaxRsbean.setAddress("http://localhost:9999/webservice");// 声明地址，注意只声明地址和端口即可
        jaxRsbean.getInInterceptors().add(new MessageInterceptor(Phase.RECEIVE));
        jaxRsbean.getOutInterceptors().add(new MessageInterceptor(Phase.SEND));
        jaxRsbean.create();// 启动
        logger.info("JAX-RS服务启动成功。。 ");
    }
}
