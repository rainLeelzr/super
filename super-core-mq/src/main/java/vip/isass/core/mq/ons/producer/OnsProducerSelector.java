package vip.isass.core.mq.ons.producer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import vip.isass.core.mq.MessageType;
import vip.isass.core.mq.core.MqMessageContext;
import vip.isass.core.mq.core.producer.MqProducer;
import vip.isass.core.mq.core.producer.ProducerSelector;
import vip.isass.core.mq.ons.config.*;
import org.springframework.stereotype.Component;
import vip.isass.core.mq.MessageType;
import vip.isass.core.mq.core.MqMessageContext;
import vip.isass.core.mq.core.producer.MqProducer;
import vip.isass.core.mq.core.producer.ProducerSelector;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rain
 */
@Component
public class OnsProducerSelector implements ProducerSelector {

    @Resource
    private OnsConfiguration onsConfiguration;

    @Resource
    private OnsProducerAutoConfiguration onsProducerAutoConfiguration;

    private Map<String, OnsProducer> producerGroupByProducerId = Collections.emptyMap();

    @Override
    public MqProducer selectProducer(final MqMessageContext mqMessageContext) {
        final RegionConfiguration regionConfiguration = OnsConfigUtil.selectRegion(
            onsConfiguration, mqMessageContext.getRegion());
        mqMessageContext.setRegion(regionConfiguration.getRegionName());

        final InstanceConfiguration instanceConfiguration = OnsConfigUtil.selectInstance(
            regionConfiguration, mqMessageContext.getInstance());
        mqMessageContext.setInstance(instanceConfiguration.getInstanceName());

        ProducerConfiguration producerConfiguration = OnsConfigUtil.selectProducer(
            regionConfiguration, instanceConfiguration, mqMessageContext.getProducerId());

        // 设置topic
        if (StrUtil.isBlank(mqMessageContext.getTopic())) {
            switch (mqMessageContext.getMessageType()) {
                case MessageType.COMMON_MESSAGE:
                    mqMessageContext.setTopic(instanceConfiguration.getCommonMessageTopic());
                    break;
                case MessageType.TIMING_MESSAGE:
                    throw new UnsupportedOperationException("未支持TIMING_MESSAGE");
                case MessageType.DELAY_MESSAGE:
                    throw new UnsupportedOperationException("未支持DELAY_MESSAGE");
                case MessageType.TRANSACTION_MESSAGE:
                    throw new UnsupportedOperationException("未支持TRANSACTION_MESSAGE");
                case MessageType.SHARDING_SEQUENTIAL_MESSAGE:
                    mqMessageContext.setTopic(instanceConfiguration.getShardingSequentialMessageTopic());
                    break;
                case MessageType.GLOBAL_SEQUENTIAL_MESSAGE:
                    mqMessageContext.setTopic(instanceConfiguration.getGlobalSequentialMessageTopic());
                    break;
                default:
                    throw new UnsupportedOperationException("未支持" + mqMessageContext.getMessageType());
            }
        }

        return producerGroupByProducerId.get(producerConfiguration.getProducerId());
    }


    @Override
    public String manufacturer() {
        return OnsConst.MANUFACTURER;
    }

    @PostConstruct
    public void init() {
        if (CollUtil.isEmpty(onsProducerAutoConfiguration.getOnsProducers())) {
            return;
        }

        producerGroupByProducerId = onsProducerAutoConfiguration.getOnsProducers()
            .stream()
            .collect(Collectors.toMap((o) -> o.getProducerProperties().getProducerId(), Function.identity()));
    }

}
