package com.wegood.core.web.config;

import com.wegood.core.web.interceptor.TraceIdInterceptor;
import com.wegood.core.web.interceptor.UriMappingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Rain
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private TraceIdInterceptor traceIdInterceptor;

    @Resource
    private UriMappingInterceptor uriMappingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceIdInterceptor).addPathPatterns("/**");
        registry.addInterceptor(uriMappingInterceptor).addPathPatterns("/**");
    }

}
