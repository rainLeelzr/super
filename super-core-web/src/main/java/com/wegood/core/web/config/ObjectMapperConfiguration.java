package com.wegood.core.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegood.core.support.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
