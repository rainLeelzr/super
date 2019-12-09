package com.wegood.core.net.session;

import com.wegood.core.net.packet.Packet;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理器，一个 Channel 对应一个 session
 * 会话管理器，只记录 channel、session、userId 的关系
 * 删除 session 时，会话管理器不会调用 channel 的 close 方法
 *
 * @author Rain
 */
@Slf4j
public class SessionManager {

    private Map<Channel, Session> sessionMapOfChannel = new ConcurrentHashMap<>();
    private Map<String, List<Session>> sessionMapOfUserId = new ConcurrentHashMap<>();

    /**
     * 添加一个 session
     */
    public void addSession(@Validated @NotNull Session session) {
        sessionMapOfChannel.putIfAbsent(session.getChannel(), session);
        if (StrUtil.isNotBlank(session.getUserId())) {
            sessionMapOfUserId
                    .computeIfAbsent(session.getUserId(), k -> new ArrayList<>(1))
                    .add(session);
        }
    }

    /**
     * 根据 channel 删除一个 session
     */
    public Session removeSession(Channel o) {
        Session remove = sessionMapOfChannel.remove(o);
        if (remove == null) {
            return null;
        }

        if (StrUtil.isNotBlank(remove.getUserId())) {
            List<Session> sessions = sessionMapOfUserId.get(remove.getUserId());
            if (sessions != null) {
                sessions.remove(remove);
            }
        }

        return remove;
    }

    public void removeAllSessionsByUserId(String userId) {
        List<Session> sessions = sessionMapOfUserId.remove(userId);
        if (CollUtil.isNotEmpty(sessions)) {
            for (Session session : sessions) {
                sessionMapOfChannel.remove(session.getChannel());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Session> T getSession(Channel channel) {
        return (T) sessionMapOfChannel.get(channel);
    }

    // todo 可能有一个用户同时登录了多台设备，则保留最近上线的设备，移除其他的设备的channel和session的对应关系。
    // todo 被移除的channel将不会再接收到业务数据，等待超时被清除。但是要考虑客户端还会保持心跳，所以不会超时的情况
    // todo 后期添加支持多端同时在线的功能，通过配置来确定是否运行用户多端同时在线
    public Session getSessionByUserId(String userId) {
        List<Session> sessions = sessionMapOfUserId.get(userId);
        if (CollUtil.isEmpty(sessions)) {
            return null;
        }

        return sessions.get(0);
    }

    public List<Session> getSessionsByUserIds(List<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }

        List<Session> sessions = new ArrayList<>(userIds.size());
        for (String userId : userIds) {
            List<Session> list = sessionMapOfUserId.get(userId);
            if (CollUtil.isEmpty(list)) {
                continue;
            }
            sessions.addAll(list);
        }

        return sessions.isEmpty() ? Collections.emptyList() : sessions;
    }

    /**
     * 获取一个 session
     * 测试专用
     */
    public Session getOneSession() {
        Set<Map.Entry<Channel, Session>> entries = sessionMapOfChannel.entrySet();
        if (CollUtil.isEmpty(entries)) {
            return null;
        }

        return entries.iterator().next().getValue();
    }

    /**
     * 根据远程终端的ip和端口获取 session
     */
    public Session getSessionByIpAndPort(@NonNull String ip, int port) {
        final String address = String.format("/%s:%s", ip, port);
        return sessionMapOfChannel.entrySet().parallelStream()
                .filter(e -> address.equals(e.getValue().getRemoteIP()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public void sendMessageByUserId(Packet packet, String userId) {
        Session session = getSessionByUserId(userId);
        session.sendMessage(packet);
    }

    /**
     * 设置userId
     */
    public void setUserId(@NonNull Session session, String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new IllegalArgumentException("userId不能为空");
        }

        String oldUserId = session.getUserId();

        // 如果旧的userId和新的userId一样，则不做处理
        if (userId.equals(oldUserId)) {
            return;
        }

        // 移除旧的userId mapping
        if (StrUtil.isNotBlank(oldUserId)) {
            List<Session> sessions = sessionMapOfUserId.get(oldUserId);
            if (sessions != null) {
                sessions.remove(session);
            }
        }

        // 添加新的userId mapping
        session.setUserId(userId);
        sessionMapOfUserId
                .computeIfAbsent(userId, k -> new ArrayList<>(1))
                .add(session);
        log.debug("设置了通道的userId：{}", session);
    }
}
