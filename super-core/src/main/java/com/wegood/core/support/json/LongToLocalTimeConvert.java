package com.wegood.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.wegood.core.support.LocalDateTimeUtil;

import java.time.LocalTime;

/**
 * @author Rain
 */
public class LongToLocalTimeConvert extends StdConverter<Long, LocalTime> {

    @Override
    public LocalTime convert(Long value) {
        return LocalDateTimeUtil.epochMilliToLocalTime(value);
    }

}
