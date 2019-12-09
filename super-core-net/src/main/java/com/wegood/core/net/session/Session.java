package com.wegood.core.net.session;


import io.netty.channel.Channel;

import java.time.LocalDateTime;

/**
 * session，Channel是代表一个具体的链接，例如在netty中，就代表是netty的channel
 *
 * @author Rain
 */
public interface Session {

    String getUserId();

    /**
     * 此方法应该只给sessionManager调用，不能给业务调到
     */
    Session setUserId(String userId);

    Channel getChannel();

    LocalDateTime getCreateTime();

    void sendMessage(Object packet);

    String getRemoteIP();

}
