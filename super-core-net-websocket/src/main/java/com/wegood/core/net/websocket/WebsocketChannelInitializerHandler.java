package com.wegood.core.net.websocket;

import com.wegood.core.net.channel.ChannelEventHandler;
import com.wegood.core.net.channel.ChannelInitializerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 客户端成功connect后执行此类来初始化化此channel的行为
 *
 * @author Rain
 */
@Slf4j
@ConditionalOnMissingBean(ChannelInitializerHandler.class)
public class WebsocketChannelInitializerHandler extends ChannelInitializerHandler<SocketChannel> {

    /**
     * 默认4分钟
     */
    @Getter
    @Value("${server.websocket.timeout:240000}")
    private int timeout;

    @Resource
    private ChannelEventHandler channelEventHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 设置tcp链路空闲超时时间
        pipeline.addLast(
                "idleStateHandler",
                new IdleStateHandler(0, 0, timeout, TimeUnit.MILLISECONDS));

        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast(channelEventHandler);
    }
}