package com.wegood.core.net.packet.impl.coder;

import com.wegood.core.net.packet.Decoder;
import com.wegood.core.net.packet.Packet;
import com.wegood.core.net.packet.BluePacket;
import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * 收到客户端的数据后，执行此类进行数据解码
 *
 * @author Rain
 */
@Slf4j
@ConditionalOnMissingBean(Decoder.class)
@Scope("prototype")
public class BlueBinaryPacketDecoder extends Decoder {

    private static final int MAX_READABLE_BYTES = 50 * 1024 * 1024;

    @Value("${spring.application.name}")
    private String appName;

    /**
     * tcp数据包的报文结构
     * ┌╌╌╌╌╌╌╌╌╌╌╌╌┬╌╌╌╌╌╌╌╌╌╌╌╌┬╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┬╌╌╌╌╌╌╌╌╌╌╌╌┐
     * ╎ fullLength ╎    type    ╎ serializeMode ╎   content  ╎
     * ├╌╌╌╌╌╌╌╌╌╌╌╌┼╌╌╌╌╌╌╌╌╌╌╌╌┼╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┼╌╌╌╌╌╌╌╌╌╌╌╌┤
     * ╎     4B     ╎     4B     ╎       4B      ╎    ≈50M    ╎
     * ├╌╌╌╌╌╌╌╌╌╌╌╌┴╌╌╌╌╌╌╌╌╌╌╌╌┴╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┴╌╌╌╌╌╌╌╌╌╌╌╌┤
     * ╎                        50M                           ╎
     * └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     */
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int readableBytes = in.readableBytes();

        log.debug("收到网络包，长度：{} 字节", readableBytes);

        if (readableBytes > MAX_READABLE_BYTES) {
            throw new IllegalArgumentException(StrUtil.format(
                    "网络包的可读数据长度大于一个网络包(50m)长度：{}", readableBytes));
        }

        // 获取网络包的前4个字节，作为一个完整报文包的数据长度
        if (readableBytes < Integer.BYTES) {
            return;
        }

        in.markReaderIndex();

        // 整个包的长度（字节）
        int fullLength = in.readInt();
        if (fullLength < 0) {
            log.error("tcp数据包的length值为负数[{}], 将强制关闭此tcp链接！{}", fullLength, ctx);
            ctx.close();
            return;
        }

        // 可读字节数小于整包的长度，说明数据未完全接收。返回，等待下次读取
        if (readableBytes < fullLength) {
            in.resetReaderIndex();
            return;
        }

        Packet packet = createPackage(ctx, in, fullLength);
        out.add(packet);

        log.debug("已拆包长度：{}", readableBytes - in.readableBytes());
    }

    @SneakyThrows
    private Packet createPackage(ChannelHandlerContext ctx, ByteBuf in, int fullLength) {
        Packet packet = new BluePacket()
                .setFullLength(fullLength)
                .setType(in.readInt())
                .setSerializeMode(in.readInt());

        int contentLength = fullLength - 12;
        if (contentLength == 0) {
            return packet;
        }

        byte[] bytes = new byte[contentLength];
        in.readBytes(bytes);
        packet.setContent(bytes);
        return packet;
    }

}
