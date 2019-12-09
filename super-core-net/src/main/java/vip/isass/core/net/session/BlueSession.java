package vip.isass.core.net.session;

import vip.isass.core.support.LocalDateTimeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * tcp
 *
 * @author Rain
 */
@Slf4j
@Accessors(chain = true)
public class BlueSession implements Session {

    /**
     * 与客户端的链接通道
     */
    @Getter
    private final Channel channel;

    /**
     * 业务系统的userId
     */
    @Getter
    @Setter
    private String userId;

    /**
     * 创建session的时间
     */
    @Getter
    @Setter
    private LocalDateTime createTime;


    public BlueSession(Channel channel) {
        this.channel = channel;
        this.createTime = LocalDateTimeUtil.now();
    }

    /**
     * 原则上系统向客户端发消息，均统一调用此方法
     */
    @Override
    public void sendMessage(Object packet) {
        if (channel == null || !channel.isActive()) {
            log.debug("channel is inactive, sedMessage cancel. session info: {}", this.toString());
            return;
        }
        channel.writeAndFlush(
            packet,
            new DefaultChannelPromise(channel, channel.eventLoop())
                .addListener((GenericFutureListener<ChannelFuture>) future -> {
                    if (future.isSuccess()) {
                        log.debug("发送给客户端[{}]成功：{}", this.getRemoteIP(), packet.toString());
                    } else {
                        log.error("发送给客户端[{}]失败。", this.getRemoteIP(), future.cause());
                    }
                })
        );
    }

    @Override
    public String getRemoteIP() {
        return getChannel().remoteAddress().toString();
    }

    public void close() {
        getChannel().close();
    }

    @Override
    public String toString() {
        return new StringBuilder("{")
            .append("\"channel\":")
            .append(channel)
            .append(",\"userId\":\"")
            .append(userId).append('\"')
            .append(",\"createTime\":")
            .append(createTime)
            .append('}')
            .toString();
    }
}
