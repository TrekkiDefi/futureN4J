package com.github.ittalks.commons.example.webservice.cxf.integration.server.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 刘春龙 on 2017/5/2.
 */
public class MessageInterceptor extends AbstractPhaseInterceptor<Message> {

    private static Logger logger = LoggerFactory.getLogger(MessageInterceptor.class);

    public MessageInterceptor(String phase) {
        super(phase);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        logger.info("================= handle Msg =================");
        logger.info(message.toString());
        if (message.getDestination() != null) {
            logger.info(message.getId() + "#" + message.getDestination().getMessageObserver());
        }
        if (message.getExchange() != null) {
            logger.info(message.getExchange().getInMessage() + "#" + message.getExchange().getInFaultMessage());
            logger.info(message.getExchange().getOutMessage() + "#" + message.getExchange().getOutFaultMessage());
        }
    }
}
