package com.wegood.core.mq.core.producer;

import com.wegood.core.mq.core.MqMessageContext;

/**
 * @author Rain
 */
public interface ProducerSelector {

    MqProducer selectProducer(MqMessageContext xMessage);

    /**
     * 提供实现的厂商
     * 例如：阿里云rocketmq、kafaka、rabbitmq
     */
    String manufacturer();

}
