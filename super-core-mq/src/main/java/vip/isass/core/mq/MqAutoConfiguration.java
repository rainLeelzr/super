package vip.isass.core.mq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Rain
 */
@Getter
@Setter
@Configuration
@ComponentScan
public class MqAutoConfiguration {

    @Value("${mq.default:}")
    private String defaultManufacturer;

    @Value("${mq.disable:}")
    private List<String> disable;

}
