package vip.isass.core.mq.core;

import vip.isass.core.mq.MessageType;
import vip.isass.core.support.SystemClock;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import vip.isass.core.mq.MessageType;
import vip.isass.core.support.SystemClock;

/**
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
public class MqMessage implements MqMessageContext {

    private String manufacturer;

    private String region;

    private String instance;

    private String producerId;

    private int messageType = MessageType.COMMON_MESSAGE;

    private String topic;

    private String tag;

    private String key;

    private String shardingKey;

    private Long consumeAtMills;

    private Long delayMills;

    private Object payload;

    private long createTime = SystemClock.now();

    @Override
    public String toString() {
        return new StringBuilder("{")
            .append("\"manufacturer\":\"")
            .append(manufacturer).append('\"')
            .append(",\"region\":\"")
            .append(region).append('\"')
            .append(",\"instance\":\"")
            .append(instance).append('\"')
            .append(",\"producerId\":\"")
            .append(producerId).append('\"')
            .append(",\"messageType\":")
            .append(messageType)
            .append(",\"topic\":\"")
            .append(topic).append('\"')
            .append(",\"tag\":\"")
            .append(tag).append('\"')
            .append(",\"key\":\"")
            .append(key).append('\"')
            .append(",\"shardingKey\":\"")
            .append(shardingKey).append('\"')
            .append(",\"consumeAtMills\":")
            .append(consumeAtMills)
            .append(",\"delayMills\":")
            .append(delayMills)
            .append(",\"payload\":")
            .append(payload)
            .append(",\"createTime\":")
            .append(createTime)
            .append('}')
            .toString();
    }

}
