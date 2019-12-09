package com.wegood.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.wegood.core.support.LocalDateTimeUtil;

import java.time.LocalDateTime;

/**
 * @author Rain
 */
public class LocalDateTimeToLongConvert extends StdConverter<LocalDateTime, Long> {

    @Override
    public Long convert(LocalDateTime value) {
        return LocalDateTimeUtil.localDateTimeToEpochMilli(value);
    }

}
