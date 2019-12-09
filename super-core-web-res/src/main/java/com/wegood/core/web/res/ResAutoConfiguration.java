package com.wegood.core.web.res;

import com.wegood.core.web.interceptor.RestTemplateInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * @author Rain
 */
@Configuration
@ComponentScan
public class ResAutoConfiguration {

    public static final int CONN_TIMEOUT_IN_MILLIS = 10_000;

    public static final int READ_TIMEOUT_IN_MILLIS = 50_000;

    @Resource
    private RestTemplateInterceptor restTemplateInterceptor;

}
