package com.wegood.core.mq.ons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Rain
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class RegionConfiguration {

    private String regionName;

    private String defaultInstance;

    private List<InstanceConfiguration> instances;

}
