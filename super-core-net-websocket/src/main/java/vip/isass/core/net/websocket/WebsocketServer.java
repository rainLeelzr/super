package vip.isass.core.net.websocket;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import vip.isass.core.net.channel.ChannelInitializerHandler;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Rain
 */
@Slf4j
@Component
public class WebsocketServer implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private ChannelInitializerHandler channelInitializerHandler;

    @Value("${server.websocket.port:20071}")
    private int port;

    private ExecutorService executorService;

    private boolean isShutdown = true;

    private synchronized boolean isShutdown() {
        return isShutdown;
    }

    private synchronized void setShutdown(boolean shutdown) {
        isShutdown = shutdown;
    }

    public void start() {
        if (!isShutdown()) {
            return;
        }

        setShutdown(false);

        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor(
                    new ThreadFactoryBuilder()
                            .setNameFormat("netty-server")
                            .setDaemon(true)
                            .build()
            );
        }

        executorService.execute(() -> {
            EventLoopGroup boss = new NioEventLoopGroup();
            EventLoopGroup worker = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(boss, worker)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.DEBUG))
                        .childHandler(channelInitializerHandler);

                ChannelFuture f = bootstrap.bind(port).sync();
                log.info("websocket 服务器启动成功！监听端口：{}", port);
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("tcp 服务器启动失败！{}", e.getMessage(), e);
            } finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
                setShutdown(true);
            }
        });

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start();
    }
}
