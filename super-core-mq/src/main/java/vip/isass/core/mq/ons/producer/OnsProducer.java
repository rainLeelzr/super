package vip.isass.core.mq.ons.producer;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import vip.isass.core.mq.MessageType;
import vip.isass.core.mq.core.MqMessageContext;
import vip.isass.core.mq.core.producer.MqProducer;
import vip.isass.core.mq.ons.config.ProducerProperties;
import vip.isass.core.serialization.GenericJackson;
import vip.isass.core.serialization.JacksonSerializable;
import vip.isass.core.support.JsonUtil;
import vip.isass.core.support.SystemClock;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import vip.isass.core.mq.MessageType;
import vip.isass.core.mq.core.MqMessageContext;
import vip.isass.core.mq.core.producer.MqProducer;
import vip.isass.core.serialization.GenericJackson;
import vip.isass.core.serialization.JacksonSerializable;
import vip.isass.core.support.JsonUtil;
import vip.isass.core.support.SystemClock;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author Rain
 */
@Slf4j
@Accessors(chain = true)
public class OnsProducer implements MqProducer {

    @Getter
    @Setter
    private ProducerProperties producerProperties;

    private Producer producer;

    private OrderProducer orderProducer;

    @Override
    public void send(MqMessageContext mqMessageContext) {
        Assert.notNull(mqMessageContext);
        Assert.notBlank(mqMessageContext.getTopic());
        Assert.notBlank(mqMessageContext.getTag());
        Assert.notNull(mqMessageContext.getPayload());

        Message msg = new Message(
            getTopic(mqMessageContext),
            mqMessageContext.getTag(),
            getBody(mqMessageContext));
        msg.setKey(mqMessageContext.getKey());

        // 设置定时、延时信息
        if (mqMessageContext.getConsumeAtMills() != null) {
            msg.setStartDeliverTime(mqMessageContext.getConsumeAtMills());
        } else if (mqMessageContext.getDelayMills() != null) {
            msg.setStartDeliverTime(SystemClock.now() + mqMessageContext.getDelayMills());
        }

        try {
            SendResult sendResult;
            if (MessageType.SHARDING_SEQUENTIAL_MESSAGE == mqMessageContext.getMessageType()) {
                sendResult = orderProducer.send(msg, mqMessageContext.getShardingKey());
            } else {
                sendResult = producer.send(msg);
            }
            // 同步发送消息，只要不抛异常就是成功
            if (sendResult != null) {
                log.debug("mq发送成功。Topic[{}], tag[{}], msgId[{}], messageKey[{}]",
                    msg.getTopic(),
                    msg.getTag(),
                    sendResult.getMessageId(),
                    mqMessageContext.getKey());
            }
        } catch (Exception e) {
            log.error("mq发送失败。tag[{}], messageKey[{}]", msg.getTag(), mqMessageContext.getKey());
            throw e;
        }
    }

    @SneakyThrows
    private byte[] getBody(MqMessageContext mqMessageContext) {
        byte[] body;
        Object payload = mqMessageContext.getPayload();
        if (payload == null) {
            body = null;
        } else if (payload instanceof JacksonSerializable) {
            body = JsonUtil.NOT_NULL_INSTANCE.writeValueAsBytes(payload);
        } else {
            body = GenericJackson.INSTANCE.serialize(mqMessageContext.getPayload());
        }
        return body;
    }

    private String getTopic(MqMessageContext mqMessageContext) {
        if (StrUtil.isNotBlank(mqMessageContext.getTopic())) {
            return mqMessageContext.getTopic();
        }
        int messageType = mqMessageContext.getMessageType();
        switch (messageType) {
            case MessageType.COMMON_MESSAGE:
                return producerProperties.getCommonMessageTopic();
            case MessageType.TIMING_MESSAGE:
                return producerProperties.getTimingMessageTopic();
            case MessageType.DELAY_MESSAGE:
                return producerProperties.getTimingMessageTopic();
            case MessageType.TRANSACTION_MESSAGE:
                throw new UnsupportedOperationException("未支持事务消息");
            case MessageType.SHARDING_SEQUENTIAL_MESSAGE:
                return producerProperties.getShardingSequentialMessageTopic();
            case MessageType.GLOBAL_SEQUENTIAL_MESSAGE:
                return producerProperties.getGlobalSequentialMessageTopic();
            default:
                throw new UnsupportedOperationException("未支持消息类型:" + messageType);
        }
    }

    @Override
    public OnsProducer init() {
        Assert.notNull(producerProperties);
        Assert.notBlank(producerProperties.getNamesrvAddr());
        Assert.notBlank(producerProperties.getAccessKey());
        Assert.notBlank(producerProperties.getSecretKey());
        Assert.notBlank(producerProperties.getProducerId());

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.NAMESRV_ADDR, producerProperties.getNamesrvAddr());
        properties.put(PropertyKeyConst.AccessKey, producerProperties.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, producerProperties.getSecretKey());
        properties.setProperty(PropertyKeyConst.GROUP_ID, producerProperties.getProducerId());
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, TimeUnit.SECONDS.toMillis(3L) + "");

        producer = ONSFactory.createProducer(properties);
        producer.start();

        orderProducer = ONSFactory.createOrderProducer(properties);
        orderProducer.start();

        return this;
    }

    @Override
    public void destroy() {
        if (producer != null) {
            producer.shutdown();
        }
        if (orderProducer != null) {
            orderProducer.shutdown();
        }
    }

}
