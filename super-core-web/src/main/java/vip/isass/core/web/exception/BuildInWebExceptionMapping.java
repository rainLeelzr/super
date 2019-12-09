package vip.isass.core.web.exception;

import cn.hutool.core.map.MapUtil;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import vip.isass.core.exception.IExceptionMapping;
import vip.isass.core.exception.code.IStatusMessage;
import vip.isass.core.exception.code.StatusMessageEnum;

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
