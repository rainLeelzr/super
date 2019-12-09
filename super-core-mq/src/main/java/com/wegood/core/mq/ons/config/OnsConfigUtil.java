package com.wegood.core.mq.ons.config;

import cn.hutool.core.util.StrUtil;

/**
 * @author Rain
 */
public class OnsConfigUtil {

    /**
     * 地域选择的优先级：
     * 1：用户传参的地域
     * 2：配置默认的地域
     * 3：配置排第一的地域
     *
     * @param region 用户传参的地域
     */
    public static RegionConfiguration selectRegion(OnsConfiguration onsConfiguration, String region) {
        RegionConfiguration regionConfiguration = null;

        // 用户传参的地域
        if (StrUtil.isNotBlank(region)) {
            regionConfiguration = onsConfiguration.getRegions().stream()
                .filter(r -> r.getRegionName().equals(region))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format(
                    "没有配置[{}]region",
                    region)));
        }

        // 配置默认的地域
        if (regionConfiguration == null && StrUtil.isNotBlank(onsConfiguration.getDefaultRegion())) {
            regionConfiguration = onsConfiguration.getRegions().stream()
                .filter(r -> r.getRegionName().equals(onsConfiguration.getDefaultRegion()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format(
                    "默认region配置错误",
                    onsConfiguration.getDefaultRegion())));
        }

        // 配置排第一的地域
        if (regionConfiguration == null) {
            regionConfiguration = onsConfiguration.getRegions().iterator().next();
        }

        if (regionConfiguration == null) {
            throw new IllegalArgumentException("没有配置地域");
        }
        return regionConfiguration;
    }

    /**
     * 实例选择的优先级：
     * 1：用户传参的实例
     * 2：配置默认的实例
     * 3：配置排第一的实例
     *
     * @param instance 用户传参的实例
     */
    public static InstanceConfiguration selectInstance(RegionConfiguration regionConfiguration, String instance) {
        InstanceConfiguration instanceConfiguration = null;

        // 用户传参的实例
        if (StrUtil.isNotBlank(instance)) {
            instanceConfiguration = regionConfiguration.getInstances().stream()
                .filter(i -> i.getInstanceName().equals(instance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format(
                    "region[{}]没有配置实例[{}]",
                    regionConfiguration.getRegionName(),
                    instance)));
        }

        // 配置默认的实例
        if (instanceConfiguration == null && StrUtil.isNotBlank(regionConfiguration.getDefaultInstance())) {
            regionConfiguration.getInstances().stream()
                .filter(i -> i.getInstanceName().equals(regionConfiguration.getDefaultInstance()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format(
                    "region[{}]默认实例配置错误",
                    regionConfiguration.getRegionName())));
        }

        // 配置排第一的实例
        if (instanceConfiguration == null) {
            instanceConfiguration = regionConfiguration.getInstances().iterator().next();
        }

        if (instanceConfiguration == null) {
            throw new IllegalArgumentException(StrUtil.format(
                "region[{}]没有配置实例",
                regionConfiguration.getRegionName()));
        }

        return instanceConfiguration;
    }

    public static ProducerConfiguration selectProducer(RegionConfiguration regionConfiguration,
                                                       InstanceConfiguration instanceConfiguration,
                                                       String producer) {
        ProducerConfiguration producerConfiguration = null;

        // 用户传参的producer
        if (StrUtil.isNotBlank(producer)) {
            producerConfiguration = instanceConfiguration.getProducers().stream()
                .filter(i -> i.getProducerId().equals(producer))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format(
                    "region[{}]instance[{}]没有配置producer[{}]",
                    regionConfiguration.getRegionName(),
                    instanceConfiguration.getInstanceName(),
                    producer)));
        }

        // 配置默认的producer
        if (producerConfiguration == null && StrUtil.isNotBlank(instanceConfiguration.getDefaultProducer())) {
            producerConfiguration = instanceConfiguration.getProducers().stream()
                .filter(p -> p.getProducerId().equals(instanceConfiguration.getDefaultProducer()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format(
                    "region[{}]instance[{}]默认producer配置错误",
                    regionConfiguration.getRegionName(),
                    instanceConfiguration.getInstanceName())));
        }

        // 配置排第一的producer
        if (producerConfiguration == null) {
            producerConfiguration = instanceConfiguration.getProducers().iterator().next();
        }

        if (producerConfiguration == null) {
            throw new IllegalArgumentException(StrUtil.format(
                "region[{}]instance[{}]没有配置producer",
                regionConfiguration.getRegionName(),
                instanceConfiguration.getInstanceName()));
        }
        return producerConfiguration;
    }
}
