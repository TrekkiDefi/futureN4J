package com.github.ittalks.commons.example.amqp;

/**
 * Created by liuchunlong on 2017/11/3.
 */
public interface MQProducer {

    /**
     * 发送消息到指定队列
     * @param queueKey 队列的key
     * @param object 发送的消息
     */
    void sendDataToQueue(String queueKey, Object object);
}
