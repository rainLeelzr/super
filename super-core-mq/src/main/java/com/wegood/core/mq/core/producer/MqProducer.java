package com.wegood.core.mq.core.producer;

import com.wegood.core.mq.core.MqMessageContext;

/**
 * @author Rain
 */
public interface MqProducer {

    MqProducer init();

    void destroy();

    void send(MqMessageContext mqMessageContext);

}
