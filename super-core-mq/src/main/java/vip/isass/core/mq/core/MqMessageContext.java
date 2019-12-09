package vip.isass.core.mq.core;

/**
 * @author Rain
 */
public interface MqMessageContext {

    /**
     * 提供实现的厂商
     * 例如：阿里云rocketmq、kafaka、rabbitmq
     */
    String getManufacturer();

    MqMessageContext setManufacturer(String manufacturer);

    String getRegion();

    MqMessageContext setRegion(String region);

    String getInstance();

    MqMessageContext setInstance(String instance);

    String getProducerId();

    MqMessageContext setProducerId(String producerId);

    int getMessageType();

    MqMessageContext setMessageType(int messageType);

    /**
     * 消息主题，一级消息类型，通过 Topic 对消息进行分类。
     * <br/>
     * 生产时用到
     */
    String getTopic();

    MqMessageContext setTopic(String topic);

    /**
     * 消息标签，二级消息类型，用来进一步区分某个 Topic 下的消息分类。
     * <br/>
     * 生产时用到
     */
    String getTag();

    MqMessageContext setTag(String tag);

    /**
     * 消息的业务标识，由消息生产者（Producer）设置，唯一标识某个业务逻辑。
     * 请尽可能全局唯一，以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发
     * <br/>
     * 生产时用到
     */
    String getKey();

    MqMessageContext setKey(String key);

    /**
     * 对于指定的一个 Topic，所有消息根据 sharding key 进行分区。
     * 同一个分区内的消息按照严格的 FIFO 顺序进行发布和消费。
     * Sharding key 是顺序消息中用来区分不同分区的关键字段
     */
    String getShardingKey();

    MqMessageContext setShardingKey(String shardingKeys);

    /**
     * Producer 将消息发送到 MQ 服务端，但并不期望这条消息立马投递，而是推迟到在当前时间点之后的某一个时间投递到 Consumer 进行消费，该消息即定时消息。
     * 如果 consumeAtMills 与 DelayMills 均赋值，则忽略 delayMills
     * <br/>
     * 生产时用到
     */
    Long getConsumeAtMills();

    MqMessageContext setConsumeAtMills(Long consumeAtMills);

    /**
     * Producer 将消息发送到 MQ 服务端，但并不期望这条消息立马投递，而是延迟一定时间后才投递到 Consumer 进行消费，该消息即延时消息。
     * 如果 consumeAtMills 与 DelayMills 均赋值，则忽略 delayMills
     * <br/>
     * 生产时用到
     */
    Long getDelayMills();

    MqMessageContext setDelayMills(Long delayMills);

    /**
     * <br/>
     * 生产时用到
     */
    Object getPayload();

    MqMessageContext setPayload(Object payload);

    long getCreateTime();

    MqMessageContext setCreateTime(long createTime);

}
