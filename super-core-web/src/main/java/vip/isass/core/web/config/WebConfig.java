package vip.isass.core.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.isass.core.web.interceptor.TraceIdInterceptor;
import vip.isass.core.web.interceptor.UriMappingInterceptor;

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
