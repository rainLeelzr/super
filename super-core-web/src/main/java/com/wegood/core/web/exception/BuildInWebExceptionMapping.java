package com.wegood.core.web.exception;

import com.wegood.core.exception.IExceptionMapping;
import com.wegood.core.exception.code.IStatusMessage;
import com.wegood.core.exception.code.StatusMessageEnum;
import cn.hutool.core.map.MapUtil;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Map;

/**
 * @author Rain
 */
@Component
public class BuildInWebExceptionMapping implements IExceptionMapping {

    private static Map<Class<? extends Exception>, IStatusMessage> exceptionMapping = MapUtil.<Class<? extends Exception>, IStatusMessage>builder()
        .put(HttpRequestMethodNotSupportedException.class, StatusMessageEnum.METHOD_NOT_ALLOWED_405)
        .put(HttpMessageNotReadableException.class, StatusMessageEnum.ILLEGAL_ARGUMENT_ERROR)
        .build();

    @Override
    public IStatusMessage getStatusCode(Class<? extends Exception> exception) {
        return exceptionMapping.get(exception);
    }
}
