package vip.isass.core.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vip.isass.core.support.UriRequestMapping;
import vip.isass.core.web.uri.UriPrefixProvider;

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
