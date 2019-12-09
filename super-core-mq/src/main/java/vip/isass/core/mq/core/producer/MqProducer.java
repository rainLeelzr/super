package vip.isass.core.mq.core.producer;

import vip.isass.core.mq.core.MqMessageContext;

/**
 * @author Rain
 */
public interface MqProducer {

    MqProducer init();

    void destroy();

    void send(MqMessageContext mqMessageContext);

}
