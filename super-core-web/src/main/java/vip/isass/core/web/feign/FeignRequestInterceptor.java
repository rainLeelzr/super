package vip.isass.core.web.feign;

import vip.isass.core.web.header.AdditionalRequestHeaderProvider;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 * 将当前线程的 request header 添加到 feign request header，
 * 实现用户的 token 在微服务之间的服务调用无缝传递
 */
@Component
public class FeignRequestInterceptor {

    @Autowired(required = false)
    private List<AdditionalRequestHeaderProvider> additionalHeaderProviders;

    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes == null ? null : attributes.getRequest();

            if (request != null) {
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();

                        // 透传 token
                        if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                            template.header(name, request.getHeader(name));
                            break;
                        }
                    }
                }
            }

            if (additionalHeaderProviders != null) {
                additionalHeaderProviders.forEach(h -> {
                    if (!h.support(template.method(), template.url())) {
                        return;
                    }

                    if (h.override()) {
                        template.header(h.getHeaderName(), h.getValue());
                        return;
                    }

                    if (request == null || request.getHeaderNames() == null) {
                        template.header(h.getHeaderName(), h.getValue());
                        return;
                    }

                    Enumeration<String> headerNames = request.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        if (h.getHeaderName().equalsIgnoreCase(headerNames.nextElement())) {
                            return;
                        }
                    }
                    template.header(h.getHeaderName(), h.getValue());

                });
            }
        };
    }
}
