package vip.isass.core.web.interceptor;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Rain
 */
@Component
public class TraceIdInterceptor implements HandlerInterceptor {

    public static final String HEADER_NAME = "isass-trace-id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.addHeader(HEADER_NAME, TraceContext.traceId());
        return true;
    }

}
