package com.github.ittalks.commons.example.amqp;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuchunlong on 2017/11/3.
 */
@Service
public class MQProducerImpl implements MQProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final static Logger LOGGER = Logger.getLogger(MQProducerImpl.class);

    /* (non-Javadoc)
     * @see com.stnts.tita.rm.api.mq.MQProducer#sendDataToQueue(java.lang.String, java.lang.Object)
     */
    @Override
    public void sendDataToQueue(String queueKey, Object object) {
        try {
            amqpTemplate.convertAndSend(queueKey, object);
        } catch (Exception e) {
            LOGGER.error(e);
        }

    }
}
