package com.wegood.core.mq.ons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Rain
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProducerProperties {

    private String regionName;

    private String instanceName;

    private String namesrvAddr;

    private String accessKey;

    private String secretKey;

    private String producerId;

    private String defaultTopic;

    private String commonMessageTopic;

    private String shardingSequentialMessageTopic;

    private String globalSequentialMessageTopic;

    private String timingMessageTopic;

}
