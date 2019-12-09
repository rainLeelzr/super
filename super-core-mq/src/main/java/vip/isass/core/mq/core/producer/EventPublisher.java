package vip.isass.core.mq.core.producer;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import vip.isass.core.mq.MqAutoConfiguration;
import vip.isass.core.mq.core.MqMessageContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rain
 */
@Slf4j
@Component
public class EventPublisher {

    @Resource
    private List<ProducerSelector> selectors;

    @Resource
    private MqAutoConfiguration mqAutoConfiguration;

    private static String DEFAULT_MANUFACTURER;
    /**
     * key: manufacturer
     */
    private static Map<String, ProducerSelector> selectorMap;

    /**
     * 发布事件
     */
    public static void send(@NonNull MqMessageContext mqMessageContext) {
        Assert.notNull(selectorMap, "EventPublisher未初始化，mq发送失败");
        MqProducer mqProducer = getMqProducer(mqMessageContext);
        Assert.notNull(mqProducer, "未找到mq配置，mq发送失败");
        mqProducer.send(mqMessageContext);
    }

    private static MqProducer getMqProducer(MqMessageContext mqMessageContext) {
        // 找实现厂商
        if (StrUtil.isBlank(mqMessageContext.getManufacturer())) {
            mqMessageContext.setManufacturer(DEFAULT_MANUFACTURER);
        }
        if (StrUtil.isBlank(mqMessageContext.getManufacturer())) {
            mqMessageContext.setManufacturer(selectorMap.entrySet().iterator().next().getKey());
        }

        // 通过厂商实现的ProducerSelector找到Producer
        ProducerSelector producerSelector = selectorMap.get(mqMessageContext.getManufacturer());
        Assert.notNull(producerSelector, "厂商[{}]未配置selector", mqMessageContext.getManufacturer());
        return producerSelector.selectProducer(mqMessageContext);
    }

    @PostConstruct
    public void init() {
        selectors.stream()
            .filter(s -> StrUtil.isBlank(s.manufacturer()))
            .findFirst()
            .ifPresent(s -> {
                throw new IllegalArgumentException(s.getClass().toGenericString() + " 的 manufacturer 不能为空");
            });

        selectorMap = selectors.stream().collect(Collectors.toMap(ProducerSelector::manufacturer, Function.identity()));

        DEFAULT_MANUFACTURER = mqAutoConfiguration.getDefaultManufacturer();
    }

}
