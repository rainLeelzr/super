package com.wegood.core.web.log.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.wegood.core.exception.UnifiedException;
import com.wegood.core.login.LoginUser;
import com.wegood.core.login.LoginUserUtil;
import com.wegood.core.web.log.model.RequestLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Rain
 * 请求日志记录器
 */
@Slf4j
@Aspect
@Component
public class RequestLogAop {

    private static final int LENGTH_LIMIT = 5000;

    @Around("execution(com.wegood.core.web.Resp *..controller..*(..))")
    public Object requestLog(ProceedingJoinPoint pjp) throws Throwable {
        return handle(pjp);
    }

    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        RequestLog requestLog = new RequestLog().setTime(DateUtil.now());

        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser != null) {
            requestLog.setLoginUser(loginUser.toString());
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            requestLog.setUri(request.getRequestURI())
                .setMethod(request.getMethod())
                .setParam(request.getParameterMap() == null
                    ? null
                    : StrUtil.subPre(
                    request.getParameterMap()
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            String[] valueArr = entry.getValue();
                            String value = ArrayUtil.isEmpty(valueArr)
                                ? ""
                                : Stream.of(valueArr)
                                .collect(Collectors.joining(","));
                            return entry.getKey() + "[" + value + "]";
                        })
                        .collect(Collectors.joining(",")),
                    LENGTH_LIMIT));

            Optional.of(request.getHeaderNames())
                .ifPresent(names -> {
                    Map<String, String> requestHeaders = new HashMap<>(16);
                    while (names.hasMoreElements()) {
                        String name = names.nextElement();
                        requestHeaders.put(name, request.getHeader(name));
                    }
                    requestLog.setRequestHeader(requestHeaders);
                });
        } else {
            requestLog.setUri("非web请求，线程：" + Thread.currentThread().getName());
        }

        TimeInterval timeInterval = null;
        try {
            timeInterval = new TimeInterval();
            Object proceed = pjp.proceed();
            requestLog.setCost((int) timeInterval.interval())
                .setResponseBody(proceed == null ? null : StrUtil.subPre(proceed.toString(), LENGTH_LIMIT));
            return proceed;
        } catch (Throwable throwable) {
            if (timeInterval != null) {
                requestLog.setCost((int) timeInterval.interval());
            }
            if (throwable instanceof UnifiedException) {
                UnifiedException unifiedException = (UnifiedException) throwable;
                requestLog.setResponseBody(unifiedException.toString());
            } else {
                requestLog.setResponseBody(StrUtil.subPre(ExceptionUtil.stacktraceToString(throwable), LENGTH_LIMIT));
            }
            throw throwable;
        } finally {
            if (requestAttributes != null) {
                HttpServletResponse response = requestAttributes.getResponse();
                Optional.ofNullable(response)
                    .ifPresent(r -> {
                        Collection<String> responseHeaderNames = r.getHeaderNames();
                        if (CollUtil.isNotEmpty(responseHeaderNames)) {
                            Map<String, String> responseHeaders = new HashMap<>(16);
                            for (String headerName : responseHeaderNames) {
                                String header = r.getHeader(headerName);
                                responseHeaders.put(headerName, header);
                            }
                            requestLog.setResponseHeader(responseHeaders);
                        }
                    });
            }
            log.debug(requestLog.toString());
        }
    }

}
