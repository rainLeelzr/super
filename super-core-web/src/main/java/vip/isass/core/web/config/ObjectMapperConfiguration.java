package vip.isass.core.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vip.isass.core.support.JsonUtil;

/**
 * @author Rain
 */
@Configuration
public class ObjectMapperConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtil.NOT_NULL_INSTANCE;
    }

}
