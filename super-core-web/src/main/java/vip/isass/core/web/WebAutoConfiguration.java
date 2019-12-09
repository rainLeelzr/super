package vip.isass.core.web;

import vip.isass.core.web.interceptor.RestTemplateInterceptor;
import feign.form.spring.converter.SpringManyMultipartFilesReader;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.Collections;


/**
 * @author Rain
 */
@Configuration
@ComponentScan
@EnableFeignClients(basePackages = "com.wegood")
public class WebAutoConfiguration {

    public static final int CONN_TIMEOUT_IN_MILLIS = 10_000;

    public static final int READ_TIMEOUT_IN_MILLIS = 50_000;

    @Resource
    private RestTemplateInterceptor restTemplateInterceptor;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(CONN_TIMEOUT_IN_MILLIS);
        httpRequestFactory.setConnectTimeout(CONN_TIMEOUT_IN_MILLIS);
        httpRequestFactory.setReadTimeout(READ_TIMEOUT_IN_MILLIS);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setInterceptors(Collections.singletonList(restTemplateInterceptor));
        return restTemplate;
    }

    @Bean
    public RestTemplate noBalancedRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(CONN_TIMEOUT_IN_MILLIS);
        httpRequestFactory.setConnectTimeout(CONN_TIMEOUT_IN_MILLIS);
        httpRequestFactory.setReadTimeout(READ_TIMEOUT_IN_MILLIS);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public HttpMessageConverter<MultipartFile[]> springManyMultipartFilesReader() {
        return new SpringManyMultipartFilesReader(4096);
    }

}
