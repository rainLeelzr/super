package vip.isass.core.net.message;

import vip.isass.core.net.tag.Tag;

import java.util.Collection;

/**
 * 消息管理器实现类
 *
 * @author Rain
 */
public class MessageManagerImpl implements MessageManager {

    @Override
    public void broadcastMessage(Packet packet) {

    }

    @Override
    public void sendMessageByUserId(Packet packet, String userId) {

    }

    @Override
    public void sendMessagesByUserId(Collection<Packet> packets, String userId) {

    }

    @Override
    public void sendMessageByUserIds(Packet packet, Collection<String> userIds) {

    }

    @Override
    public void sendMessagesByUserIds(Collection<Packet> packets, Collection<String> userIds) {

    }

    @Override
    public void sendMessageByTagKey(Packet packet, String tagKey) {

    }

    @Override
    public void sendMessagesByTagKey(Collection<Packet> packets, String tagKey) {

    }

    @Override
    public void sendMessageByTagKeys(Packet packet, Collection<String> tagKeys) {

    }

    @Override
    public void sendMessagesByTagKeys(Collection<Packet> packets, Collection<String> tagKeys) {

    }

    @Override
    public void sendMessageByTagPair(Packet packet, String tagKey, String tagValue) {

    }

    @Override
    public void sendMessagesByTagPair(Collection<Packet> packets, String tagKey, String tagValue) {

    }

    @Override
    public void sendMessagesByTagPair(Collection<Packet> packets, Tag<?> tag) {

    }

    @Override
    public void sendMessageByTagPairs(Packet packet, Collection<Tag<?>> tags) {

    }

    @Override
    public void sendMessagesByTagPairs(Collection<Packet> packets, Collection<Tag<?>> tags) {

    }
}
