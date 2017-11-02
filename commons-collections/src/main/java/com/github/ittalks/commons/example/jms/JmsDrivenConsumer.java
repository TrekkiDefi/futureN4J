package com.github.ittalks.commons.example.jms;

import com.github.ittalks.commons.example.jms.model.Msg;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/11/2.
 *
 * @Description 基于消息驱动的POJO，详细配置见{@code spring-jms.xml}
 */
@Component
public class JmsDrivenConsumer {

    private static final Logger logger = Logger.getLogger(JmsConsumer.class.getName());

    public void consume(Msg msg) {
        // [org.springframework.jms.listener.DefaultMessageListenerContainer#0-1], Received Msg：[Msg{msg='"hello fnpac"'}]
        logger.info("[" + Thread.currentThread().getName() + "], Received Msg：[" + msg + "]");
    }
}
