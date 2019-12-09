package vip.isass.core.net.packet.impl.coder;

import vip.isass.core.net.packet.Encoder;
import vip.isass.core.net.packet.Packet;
import vip.isass.core.serialization.SerializeMode;
import vip.isass.core.support.JsonUtil;
import com.google.protobuf.GeneratedMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.EmptyArrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * 发送数据给客户端的时，执行此类进行编码
 *
 * @author Rain
 */
@Slf4j
@ChannelHandler.Sharable
@ConditionalOnMissingBean(Encoder.class)
public class BlueBinaryPacketEncoder extends Encoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        out.writeBytes(encode(packet));
    }

    @SneakyThrows
    public static ByteBuf encode(Packet packet) {
        byte[] contentBytes;
        Object contentObj = packet.getContent();
        if (contentObj == null) {
            contentBytes = EmptyArrays.EMPTY_BYTES;
        } else if (contentObj instanceof byte[]) {
            contentBytes = (byte[]) packet.getContent();
        } else if (SerializeMode.JSON.getCode().equals(packet.getSerializeMode())) {
            if (contentObj instanceof String) {
                contentBytes = (((String) contentObj).getBytes(UTF_8));
            } else {
                contentBytes = JsonUtil.DEFAULT_INSTANCE.writeValueAsBytes(contentObj);
            }
        } else if (SerializeMode.PROTOBUF2.getCode().equals(packet.getSerializeMode())) {
            if (contentObj instanceof GeneratedMessage) {
                contentBytes = ((GeneratedMessage) contentObj).toByteArray();
            } else {
                throw new IllegalArgumentException("序列化模式是pb, 但content不是GeneratedMessage");
            }
        } else {
            throw new UnsupportedOperationException("编码器不支持的序列化类型:" + packet.getSerializeMode());
        }

        packet.setFullLength(12 + contentBytes.length);
        ByteBuf byteBuf = Unpooled.buffer()
                .writeInt(packet.getFullLength())
                .writeInt(packet.getType())
                .writeInt(packet.getSerializeMode() == null ? SerializeMode.JSON.getCode() : packet.getSerializeMode())
                .writeBytes(contentBytes);
        log.debug("编码后字节大小:{} 字节", byteBuf.readableBytes());
        return byteBuf;
    }

}
