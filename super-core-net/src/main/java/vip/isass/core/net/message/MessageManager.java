package vip.isass.core.net.message;

import vip.isass.core.net.tag.Tag;

import java.util.Collection;

/**
 * 消息管理器，负责消息的发送
 *
 * @author Rain
 */
public interface MessageManager {

    /**
     * 广播消息
     *
     * @param packet 消息体
     */
    void broadcastMessage(Packet packet);

    /**
     * 发送消息给指定的用户
     *
     * @param packet 消息体
     * @param userId 用户 id
     */
    void sendMessageByUserId(Packet packet, String userId);

    /**
     * 发送多条消息给指定的用户
     *
     * @param packets 消息体集合
     * @param userId  用户 id
     */
    void sendMessagesByUserId(Collection<Packet> packets, String userId);

    /**
     * 发送消息给指定的用户集合
     *
     * @param packet  消息体
     * @param userIds 用户 id 集合
     */
    void sendMessageByUserIds(Packet packet, Collection<String> userIds);

    /**
     * 发送多条消息给指定的用户集合
     *
     * @param packets 消息体集合
     * @param userIds 用户 id 集合
     */
    void sendMessagesByUserIds(Collection<Packet> packets, Collection<String> userIds);

    /**
     * 发送消息给拥有指定标签键的用户
     *
     * @param packet 消息体
     * @param tagKey 标签键
     */
    void sendMessageByTagKey(Packet packet, String tagKey);

    /**
     * 发送多条消息给拥有指定标签键的用户
     *
     * @param packets 消息体集合
     * @param tagKey  标签键
     */
    void sendMessagesByTagKey(Collection<Packet> packets, String tagKey);

    /**
     * 发送消息给拥有指定标签键集合的用户
     *
     * @param packet  消息体
     * @param tagKeys 标签键集合
     */
    void sendMessageByTagKeys(Packet packet, Collection<String> tagKeys);

    /**
     * 发送多条消息给拥有指定标签键集合的用户
     *
     * @param packets 消息体集合
     * @param tagKeys 标签键集合
     */
    void sendMessagesByTagKeys(Collection<Packet> packets, Collection<String> tagKeys);

    /**
     * 发送消息给拥有指定标签键值对的用户
     *
     * @param packet   消息体
     * @param tagKey   标签键
     * @param tagValue 标签值
     */
    void sendMessageByTagPair(Packet packet, String tagKey, String tagValue);

    /**
     * 发送多条消息给拥有指定标签键值对的用户
     *
     * @param packets  消息体集合
     * @param tagKey   标签键
     * @param tagValue 标签值
     */
    void sendMessagesByTagPair(Collection<Packet> packets, String tagKey, String tagValue);

    /**
     * 发送多条消息给拥有指定标签键值对的用户
     *
     * @param packets 消息体集合
     * @param tag     标签键值对
     */
    void sendMessagesByTagPair(Collection<Packet> packets, Tag<?> tag);

    /**
     * 发送消息给拥有指定标签键值对集合的用户
     *
     * @param packet 消息体
     * @param tags   标签键值对集合
     */
    void sendMessageByTagPairs(Packet packet, Collection<Tag<?>> tags);

    /**
     * 发送消息给拥有指定标签键值对集合的用户
     *
     * @param packets 消息体集合
     * @param tags    标签键值对集合
     */
    void sendMessagesByTagPairs(Collection<Packet> packets, Collection<Tag<?>> tags);

}
