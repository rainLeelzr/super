package vip.isass.core.mq.ons.producer;

import cn.hutool.core.collection.CollUtil;
import vip.isass.core.mq.ons.config.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rain
 */
@Slf4j
@Configuration
public class OnsProducerAutoConfiguration {

    @Resource
    private OnsConfiguration onsConfiguration;

    @Getter
    private List<OnsProducer> onsProducers;

    @PostConstruct
    public void init() {
        onsProducers = new ArrayList<>();
        List<RegionConfiguration> regions = onsConfiguration.getRegions();
        if (regions == null) {
            log.warn("can not found any mq properties");
            return;
        }
        for (RegionConfiguration region : regions) {
            for (InstanceConfiguration instanceConfiguration : region.getInstances()) {
                for (ProducerConfiguration producerConfiguration : instanceConfiguration.getProducers()) {
                    onsProducers.add(new OnsProducer()
                        .setProducerProperties(new ProducerProperties()
                            .setRegionName(region.getRegionName())
                            .setInstanceName(instanceConfiguration.getInstanceName())
                            .setNamesrvAddr(instanceConfiguration.getNamesrvAddr())
                            .setAccessKey(instanceConfiguration.getAccessKey())
                            .setSecretKey(instanceConfiguration.getSecretKey())
                            .setProducerId(producerConfiguration.getProducerId())
                            .setDefaultTopic(instanceConfiguration.getDefaultTopic())
                            .setCommonMessageTopic(instanceConfiguration.getCommonMessageTopic())
                            .setGlobalSequentialMessageTopic(instanceConfiguration.getGlobalSequentialMessageTopic())
                            .setShardingSequentialMessageTopic(instanceConfiguration.getShardingSequentialMessageTopic())
                            .setTimingMessageTopic(instanceConfiguration.getTimingMessageTopic()))
                        .init());
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (CollUtil.isEmpty(onsProducers)) {
            return;
        }
        onsProducers.stream()
            .filter(Objects::nonNull)
            .forEach(OnsProducer::destroy);
    }

}
