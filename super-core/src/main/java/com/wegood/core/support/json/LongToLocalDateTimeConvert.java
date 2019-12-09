package com.wegood.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.wegood.core.support.LocalDateTimeUtil;

import java.time.LocalDateTime;

/**
 * @author Rain
 */
public class LongToLocalDateTimeConvert extends StdConverter<Long, LocalDateTime> {

    @Override
    public LocalDateTime convert(Long value) {
        return LocalDateTimeUtil.epochMilliToLocalDateTime(value);
    }

}
