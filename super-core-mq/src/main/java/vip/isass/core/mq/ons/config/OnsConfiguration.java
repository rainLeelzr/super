package vip.isass.core.mq.ons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Rain
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("mq.ons")
public class OnsConfiguration {

    private String defaultRegion;

    private List<RegionConfiguration> regions;

}
