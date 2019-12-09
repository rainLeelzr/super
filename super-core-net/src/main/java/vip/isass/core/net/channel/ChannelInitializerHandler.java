package vip.isass.core.net.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author Rain
 */
public abstract class ChannelInitializerHandler<C extends Channel> extends ChannelInitializer<C> {
}
