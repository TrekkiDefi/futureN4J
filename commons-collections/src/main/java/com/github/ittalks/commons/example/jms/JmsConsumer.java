package com.github.ittalks.commons.example.jms;

import com.github.ittalks.commons.example.jms.model.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.support.JmsUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/11/2.
 *
 * @Description 基于{@link #consume()}方法阻塞获取消息
 */
@Component
public class JmsConsumer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = Logger.getLogger(JmsConsumer.class.getName());

    private final JmsOperations template;

    @Autowired
    public JmsConsumer(JmsOperations template) {
        this.template = template;
    }

    private void consume() {
        while (true) {
            ObjectMessage message = (ObjectMessage) template.receive("barrage.queue");
            try {
                Msg msg = (Msg) message.getObject();
                // [FnExecutor-1], Received Msg：[Msg{msg='"hello fnpac"'}]
                logger.info("[" + Thread.currentThread().getName() + "], Received Msg：[" + msg + "]");
            } catch (JMSException e) {
                throw JmsUtils.convertJmsAccessException(e);
            }
        }
    }

    @Async
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            consume();
        }
    }
}
