package com.wegood.core.mq.ons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 阿里云mq实例配置
 *
 * @author Rain
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class InstanceConfiguration {

    private String instanceName;

    private String namesrvAddr;

    private String accessKey;

    private String secretKey;

    private String defaultTopic;

    private String commonMessageTopic;

    private String shardingSequentialMessageTopic;

    private String globalSequentialMessageTopic;

    private String timingMessageTopic;

    private String defaultProducer;

    private List<ProducerConfiguration> producers;

}
