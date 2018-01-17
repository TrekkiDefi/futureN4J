package com.github.ittalks.fn;

import com.github.ittalks.commons.example.jms.model.Msg;
import com.github.ittalks.fn.utils.FnAppTest;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuchunlong on 2017/11/4.
 */
public class RabbitMQTest extends FnAppTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsg() {

        rabbitTemplate.convertAndSend("rabbit.queue.key", new Msg("hello fnpac"));
    }
}
