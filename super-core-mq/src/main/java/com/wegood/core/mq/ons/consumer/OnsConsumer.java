package com.wegood.core.mq.ons.consumer;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.ons.api.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.wegood.core.mq.MessageType;
import com.wegood.core.mq.core.SubscribeModel;
import com.wegood.core.mq.core.consumer.EventListener;
import com.wegood.core.mq.core.consumer.MqConsumer;
import com.wegood.core.mq.ons.config.*;
import com.wegood.core.serialization.GenericJackson;
import com.wegood.core.serialization.JacksonSerializable;
import com.wegood.core.support.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author Rain
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@Scope("prototype")
@Component
public class OnsConsumer implements MqConsumer {

    private EventListener eventListener;

    private SubscribeModel subscribeModel;

    private String region;

    private String instance;

    private String producerId;

    private String consumerId;

    private String topic;

    private String tag;

    private Integer consumeThreadNumber;

    private Consumer consumer;

    private Object runtimeBean;

    private Method runtimeMethod;

    @Resource
    private OnsConfiguration onsConfiguration;

    @Override
    public String getManufacturer() {
        return OnsConst.MANUFACTURER;
    }

    @Override
    public void subscribe() {
        log.info("开始订阅事件,consumerId[{}]", consumerId);
        Assert.notBlank(consumerId);

        RegionConfiguration regionConfiguration = OnsConfigUtil.selectRegion(onsConfiguration, region);
        this.region = regionConfiguration.getRegionName();

        InstanceConfiguration instanceConfiguration = OnsConfigUtil.selectInstance(regionConfiguration, instance);
        this.instance = instanceConfiguration.getInstanceName();

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.NAMESRV_ADDR, instanceConfiguration.getNamesrvAddr());
        properties.put(PropertyKeyConst.AccessKey, instanceConfiguration.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, instanceConfiguration.getSecretKey());
        properties.put(PropertyKeyConst.GROUP_ID, consumerId);

        if (consumeThreadNumber != null && consumeThreadNumber != -1) {
            properties.put(PropertyKeyConst.ConsumeThreadNums, consumeThreadNumber);
        }

        // 订阅方式
        switch (getSubscribeModel()) {
            case BROADCASTING:
                properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
                break;
            case CLUSTERING:
                properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
                break;
            default:
        }

        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(
            parseTopic(instanceConfiguration),
            tag,
            (message, context) -> {
                log.debug("收到mq消息：{}", message);
                Object[] realParameterData;
                try {
                    realParameterData = genParameter(message);
                } catch (Exception e) {
                    log.error("反序列化mq消息错误，此消息将标记为消费成功：{}，", e.getMessage(), e);
                    return Action.CommitMessage;
                }
                try {
                    runtimeMethod.invoke(runtimeBean, realParameterData);
                    return Action.CommitMessage;
                } catch (Throwable e) {
                    log.error("mq消费错误：{}", e.getMessage(), e);
                    Throwable unwrap = ExceptionUtil.unwrap(e);
                    log.error("{}", unwrap.getMessage(), unwrap);
                    return Action.ReconsumeLater;
                }
            });
        consumer.start();
    }

    private String parseTopic(InstanceConfiguration instanceConfiguration) {
        if (StrUtil.isNotBlank(eventListener.topic())) {
            return eventListener.topic();
        }
        int messageType = eventListener.messageType();
        switch (messageType) {
            case MessageType.COMMON_MESSAGE:
                return instanceConfiguration.getCommonMessageTopic();
            case MessageType.TIMING_MESSAGE:
                return instanceConfiguration.getTimingMessageTopic();
            case MessageType.DELAY_MESSAGE:
                return instanceConfiguration.getTimingMessageTopic();
            case MessageType.TRANSACTION_MESSAGE:
                throw new UnsupportedOperationException("未支持事务消息");
            case MessageType.SHARDING_SEQUENTIAL_MESSAGE:
                return instanceConfiguration.getShardingSequentialMessageTopic();
            case MessageType.GLOBAL_SEQUENTIAL_MESSAGE:
                return instanceConfiguration.getGlobalSequentialMessageTopic();
            default:
                throw new UnsupportedOperationException("未支持消息类型:" + messageType);
        }
    }

    @SneakyThrows
    private Object[] genParameter(Message message) {
        Class<?>[] declaringParameters = runtimeMethod.getParameterTypes();
        Object[] realParameterData = null;
        if (declaringParameters.length > 0) {
            realParameterData = new Object[declaringParameters.length];

            for (int i = 0; i < declaringParameters.length; i++) {
                Class<?> declaringParameter = declaringParameters[i];
                if (Message.class.isAssignableFrom(declaringParameter)) {
                    realParameterData[i] = message;
                } else if (JacksonSerializable.class.isAssignableFrom(declaringParameter)) {
                    Field field = ReflectUtil.getField(declaringParameter, "TYPE_REFERENCE");
                    Assert.notNull(field, "[{}]是JacksonSerializable的实现，但没有TYPE_REFERENCE方法", declaringParameter);
                    TypeReference typeReference = (TypeReference) field.get(null);
                    Object obj = JsonUtil.DEFAULT_INSTANCE.readValue(message.getBody(), typeReference);
                    realParameterData[i] = obj;
                } else {
                    byte[] body = message.getBody();
                    Object obj;
                    obj = GenericJackson.INSTANCE.deserialize(body);
                    realParameterData[i] = obj;
                }
            }
        }
        return realParameterData;
    }

    @PreDestroy
    public void destroy() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }

}
