package vip.isass.core.net.request;

import vip.isass.core.net.packet.Packet;
import vip.isass.core.net.session.Session;
import vip.isass.core.support.JsonUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import vip.isass.core.net.session.Session;

/**
 * 一个网络包请求就是一个系统的事件.类似一个task任务
 *
 * @author Rain
 */
@Slf4j
@Getter
@Accessors(chain = true)
public class Request<P extends Packet, S extends Session> {

    /**
     * 分组
     */
    private String group;

    private P packet;

    private S session;

    private Protocol requestProtocol;

    public Request(P packet, S session, Protocol requestProtocol) {
        this.packet = packet;
        this.session = session;
        this.requestProtocol = requestProtocol;
    }

    public Request(String group, P packet, S session, Protocol requestProtocol) {
        this.group = group;
        this.packet = packet;
        this.session = session;
        this.requestProtocol = requestProtocol;
    }

    @SneakyThrows
    public void sendResponse(Packet packet) {
        if (requestProtocol == Protocol.WEBSOCKET) {
            String json = JsonUtil.DEFAULT_INSTANCE.writeValueAsString(packet);
            this.session.sendMessage(new TextWebSocketFrame(json));
        } else if (requestProtocol == Protocol.TCP) {
            this.session.sendMessage(packet);
        }
    }

    public enum Protocol {
        TCP,
        WEBSOCKET;
    }

    @Override
    public String toString() {
        return new StringBuilder("{")
                .append("\"group\":\"")
                .append(group).append('\"')
                .append(",\"packet\":")
                .append(packet)
                .append(",\"session\":")
                .append(session)
                .append(",\"requestProtocol\":")
                .append(requestProtocol)
                .append('}')
                .toString();
    }
}
