package com.wegood.core.mq.core.consumer;


import com.wegood.core.mq.core.SubscribeModel;
import lombok.NonNull;

import java.lang.reflect.Method;

/**
 * @author Rain
 */
public interface MqConsumer {

    MqConsumer setEventListener(EventListener eventListener);

    EventListener getEventListener();

    @NonNull String getManufacturer();

    SubscribeModel getSubscribeModel();

    MqConsumer setSubscribeModel(SubscribeModel subscribeModel);

    String getConsumerId();

    MqConsumer setConsumerId(String consumerId);

    String getTopic();

    MqConsumer setTopic(String topic);

    String getTag();

    MqConsumer setTag(String tag);

    Integer getConsumeThreadNumber();

    MqConsumer setConsumeThreadNumber(Integer consumeThreadNumber);

    void subscribe();

    Object getRuntimeBean();

    MqConsumer setRuntimeBean(Object runtimeBean);

    Method getRuntimeMethod();

    MqConsumer setRuntimeMethod(Method runtimeMethod);

}
