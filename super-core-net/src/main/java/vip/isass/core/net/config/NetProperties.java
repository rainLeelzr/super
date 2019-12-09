package vip.isass.core.net.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Rain
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ipas")
public class NetProperties {

    private int restTemplateTimeOut = 20_000;

}
