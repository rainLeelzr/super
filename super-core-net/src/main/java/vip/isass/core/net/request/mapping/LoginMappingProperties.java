package vip.isass.core.net.request.mapping;

import vip.isass.core.net.packet.impl.HttpContent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Rain
 */
@ConfigurationProperties(prefix = "server")
@Getter
@Setter
public class LoginMappingProperties {

    Map<String, HttpContent.HttpMethodEnum> loginMappings;

}
