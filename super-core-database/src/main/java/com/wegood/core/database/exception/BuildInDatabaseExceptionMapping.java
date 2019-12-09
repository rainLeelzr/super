package com.wegood.core.database.exception;

import com.wegood.core.exception.IExceptionMapping;
import com.wegood.core.exception.code.IStatusMessage;
import com.wegood.core.exception.code.StatusMessageEnum;
import cn.hutool.core.map.MapUtil;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Rain
 */
@Component
public class BuildInDatabaseExceptionMapping implements IExceptionMapping {

    private static Map<Class<? extends Exception>, IStatusMessage> exceptionMapping = MapUtil.<Class<? extends Exception>, IStatusMessage>builder()
        .put(DuplicateKeyException.class, StatusMessageEnum.DUPLICATE_KEY)
        .put(TooManyResultsException.class, StatusMessageEnum.TOO_MANY_RESULT)
        .build();

    @Override
    public IStatusMessage getStatusCode(Class<? extends Exception> exception) {
        return exceptionMapping.get(exception);
    }
}
