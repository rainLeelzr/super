package vip.isass.core.net.socketio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "socketio")
public class SocketIoConfiguration {

    private int tcpPort = 20001;

}
