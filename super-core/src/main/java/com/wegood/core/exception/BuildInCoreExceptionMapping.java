package com.wegood.core.exception;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.map.MapUtil;
import com.wegood.core.exception.code.IStatusMessage;
import com.wegood.core.exception.code.StatusMessageEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

/**
 * @author Rain
 */
@Component
public class BuildInCoreExceptionMapping implements IExceptionMapping {

    private static Map<Class<? extends Exception>, IStatusMessage> exceptionMapping = MapUtil.<Class<? extends Exception>, IStatusMessage>builder()
        .put(IllegalArgumentException.class, StatusMessageEnum.ILLEGAL_ARGUMENT_ERROR)
        .put(AlreadyPresentException.class, StatusMessageEnum.ALREADY_PRESENT)
        .put(AbsentException.class, StatusMessageEnum.ABSENT)
        .put(UnsupportedOperationException.class, StatusMessageEnum.UN_SUPPORT_OPERATION)
        .put(MethodArgumentNotValidException.class, StatusMessageEnum.ILLEGAL_ARGUMENT_ERROR)
        .put(ValidateException.class, StatusMessageEnum.ILLEGAL_ARGUMENT_ERROR)
        .build();

    @Override
    public IStatusMessage getStatusCode(Class<? extends Exception> exception) {
        return exceptionMapping.get(exception);
    }
}
