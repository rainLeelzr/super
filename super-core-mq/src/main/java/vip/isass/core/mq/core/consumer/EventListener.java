package vip.isass.core.mq.core.consumer;

import vip.isass.core.mq.MessageType;
import vip.isass.core.mq.core.SubscribeModel;
import org.springframework.stereotype.Component;
import vip.isass.core.mq.MessageType;

import java.lang.annotation.*;


/**
 * @author Rain
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EventListener {

    /**
     * 提供实现的厂商
     * 例如：阿里云rocketmq、kafaka、rabbitmq
     */
    String manufacturer() default "";

    String region() default "";

    String instance() default "";

    String consumerId();

    /**
     * 消费模式
     */
    SubscribeModel subscribeModel() default SubscribeModel.CLUSTERING;

    int messageType() default MessageType.COMMON_MESSAGE;

    /**
     * 消息主题，一级消息类型，通过 Topic 对消息进行分类。
     */
    String topic() default "";

    /**
     * 消息标签，二级消息类型，用来进一步区分某个 Topic 下的消息分类。
     */
    String tag() default "*";

    /**
     * 设置 Consumer 实例的消费线程数，默认值
     */
    int consumeThreadNumber() default -1;

    /**
     * 消息的业务标识，由消息生产者（Producer）设置，唯一标识某个业务逻辑。(一般与tag同值)
     */
    String key() default "";

}
