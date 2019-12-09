package com.wegood.core.net.channel;

import com.wegood.core.net.packet.Packet;
import com.wegood.core.net.request.Request;
import com.wegood.core.net.request.RequestManager;
import com.wegood.core.net.session.BlueSession;
import com.wegood.core.net.session.Session;
import com.wegood.core.net.session.SessionManager;
import com.wegood.core.support.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.SneakyThrows;
import org.slf4j.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Rain
 */
public interface ChannelEventHandler extends ChannelInboundHandler {

    SessionManager getSessionManager();

    RequestManager getRequestManager();

    Logger getLogger();

    default void channelActive0(ChannelHandlerContext ctx) throws Exception {
        // 新的channel激活时，绑定channel与session的关系
        Channel channel = ctx.channel();

        Session session = new BlueSession(channel);
        getSessionManager().addSession(session);

        getLogger().debug("服务器接收到客户端的连接，客户端ip：{}", channel.remoteAddress());

        channelRegistered(ctx);
    }

    @SneakyThrows
    default void channelRead1(ChannelHandlerContext cx, Packet packet, Request.Protocol protocol) {
        Session session = getSessionManager().getSession(cx.channel());
        if (session == null) {
            getLogger().error("channelRead失败，channel对应的session为null");
            return;
        }

        if (Packet.Type.HEART_BEAT.getCode().equals(packet.getType())) {
            getLogger().debug("收到心跳包");
            return;
        } else if (Packet.Type.SET_USER_ID.getCode().equals(packet.getType())) {
            Object userId = packet.getContent();
            if (userId == null) {
                getLogger().error("处理Packet.Type.SET_USER_ID请求包时，content内容为null。忽略此包。");
                return;
            }
            String userIdStr;
            if (Request.Protocol.TCP == protocol) {
                userIdStr = new String((byte[]) userId, UTF_8);
            } else if (Request.Protocol.WEBSOCKET == protocol) {
                userIdStr = (String) userId;
            } else {
                throw new UnsupportedOperationException("不支持的userId请求包的请求协议：" + protocol);
            }

            getSessionManager().setUserId(session, userIdStr);

            packet.setType(Packet.Type.PUSH.getCode());
            packet.setContent("绑定此通道的userId成功！");

            if (Request.Protocol.TCP == protocol) {
                session.sendMessage(packet);
            } else {
                String json = JsonUtil.DEFAULT_INSTANCE.writeValueAsString(packet);
                session.sendMessage(new TextWebSocketFrame(json));
            }

            return;
        }

        Request bizRequest = new Request<>(packet, session, protocol);
        getRequestManager().addRequest(bizRequest);
    }

    default void userEventTriggered0(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.ALL_IDLE) {
                getLogger().debug(
                        "channel超时没有读写操作，将主动关闭链接通道！session={}",
                        getSessionManager().getSession(ctx.channel()).toString());
                ctx.close();
            }
        }
    }

    default void exceptionCaught0(ChannelHandlerContext ctx, Throwable cause) {
        if (!"远程主机强迫关闭了一个现有的连接。".equals(cause.getMessage())) {
            getLogger().error(cause.getMessage(), cause);
        }
        if (cause instanceof java.io.IOException) {
            getLogger().error(cause.getMessage());
            return;
        } else {
            getLogger().error(cause.getMessage(), cause);
        }
        ctx.close();
    }

    default void channelInactive0(ChannelHandlerContext ctx) {
        ctx.fireChannelInactive();
        Channel channel = ctx.channel();
        if (channel != null) {
            Session session = getSessionManager().removeSession(ctx.channel());
            getLogger().debug("成功关闭了一个websocket连接：session={}", session.toString());
        }

        // todo 分发用户下线事件
    }
}
