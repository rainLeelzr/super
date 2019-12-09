package com.wegood.core.net.tcp;

import com.wegood.core.net.channel.ChannelEventHandler;
import com.wegood.core.net.packet.Packet;
import com.wegood.core.net.request.Request;
import com.wegood.core.net.request.RequestManager;
import com.wegood.core.net.session.SessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * channel事件被触发时，执行此类对应的事件处理方法
 *
 * @author Rain
 */
@Slf4j
@ChannelHandler.Sharable
@ConditionalOnMissingBean(ChannelEventHandler.class)
public class TcpChannelEventHandler extends ChannelInboundHandlerAdapter implements ChannelEventHandler {

    @Resource
    @Getter
    private SessionManager sessionManager;

    @Resource
    @Getter
    private RequestManager requestManager;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelActive0(ctx);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext cx, Object object) {
        channelRead1(cx, (Packet) object, Request.Protocol.TCP);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        ctx.fireUserEventTriggered(evt);
        userEventTriggered0(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        exceptionCaught0(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        channelInactive0(ctx);
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>(5000000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            map.put(i + "", i);
        }
        System.out.println("put结束，耗时：" + (System.currentTimeMillis() - start) + " ms");
        System.out.println("map.size=" + map.size());

        start = System.currentTimeMillis();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (key.equals("2000")) {
                System.out.println(true);
            }
        }
        System.out.println("遍历结束，耗时：" + (System.currentTimeMillis() - start) + " ms");

        start = System.currentTimeMillis();
        Integer integer = map.get("2000");
        System.out.println(integer);
        System.out.println("get，耗时：" + (System.currentTimeMillis() - start) + " ms");
    }

}