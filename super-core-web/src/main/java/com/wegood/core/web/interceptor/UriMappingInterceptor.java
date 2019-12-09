package com.wegood.core.web.interceptor;

import com.wegood.core.support.UriRequestMapping;
import com.wegood.core.web.uri.UriPrefixProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Rain
 */
@Component
public class UriMappingInterceptor implements HandlerInterceptor {

    @Resource
    private UriPrefixProvider uriPrefixProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String mapping = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        response.addHeader(UriRequestMapping.MAPPING_KEY, uriPrefixProvider.getUriPrefix() + mapping);
        return true;
    }

}
