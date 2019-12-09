package vip.isass.core.net.tcp;

import vip.isass.core.net.channel.ChannelEventHandler;
import vip.isass.core.net.channel.ChannelInitializerHandler;
import vip.isass.core.net.packet.Decoder;
import vip.isass.core.net.packet.Encoder;
import vip.isass.core.net.packet.impl.coder.BlueBinaryPacketDecoder;
import vip.isass.core.support.SpringContextUtil;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 客户端成功connect后执行此类来初始化化此channel的行为
 *
 * @author Rain
 */
@ConditionalOnMissingBean(ChannelInitializerHandler.class)
public class TcpChannelInitializerHandler extends ChannelInitializerHandler<SocketChannel> {

    @Getter
    @Value("${tcp.server.socket.timeout:120000}")
    private int timeout;

    @Resource
    private Encoder encoder;

    @Resource
    private ChannelEventHandler channelEventHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 设置tcp链路空闲超时时间
        pipeline.addLast(
                "idleStateHandler",
                new IdleStateHandler(0, 0, timeout, TimeUnit.MILLISECONDS));

        // 添加解码器
        Decoder decoder = SpringContextUtil.isInitialized() ? SpringContextUtil.getBean(Decoder.class) : new BlueBinaryPacketDecoder();
        pipeline.addLast("decoder", decoder);

        // 添加事件的处理方法
        pipeline.addLast(channelEventHandler);

        // 添加编码器
        pipeline.addLast("encoder", encoder);

    }
}