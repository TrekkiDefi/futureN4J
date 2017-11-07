package com.github.ittalks.commons.example.amqp;

import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Created by liuchunlong on 2017/11/3.
 */
@Component
public class QueueListenter implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(QueueListenter.class);

    @Override
    public void onMessage(Message message, byte[] bytes) {
        LOGGER.info(message.toString());
    }
}
