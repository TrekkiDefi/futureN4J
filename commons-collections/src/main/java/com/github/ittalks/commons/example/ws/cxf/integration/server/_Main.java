package com.github.ittalks.commons.example.ws.cxf.integration.server;

import com.github.ittalks.commons.example.ws.cxf.integration.server.interceptor.MessageInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 刘春龙 on 2017/11/1.
 */
public class _Main {

    private static Logger logger = LoggerFactory.getLogger(_Main.class);

    public static void main(String[] args) {
        JaxWsServerFactoryBean wsBean = new JaxWsServerFactoryBean();
        wsBean.setServiceClass(ComplexUserService.class);
        wsBean.getInInterceptors().add(new MessageInterceptor(Phase.RECEIVE));
        wsBean.getOutInterceptors().add(new MessageInterceptor(Phase.SEND));
        wsBean.setAddress("http://localhost:9999/ws");
        wsBean.create(); // 内部使用jetty服务器做为支持
        logger.info("JAX_WS服务启动成功。。 ");
    }
}
