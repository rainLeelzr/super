package com.wegood.core.mq.ons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 阿里云mq实例配置
 *
 * @author Rain
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProducerConfiguration {

    private String producerId;

}
