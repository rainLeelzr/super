package com.wegood.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.wegood.core.support.LocalDateTimeUtil;

import java.time.LocalTime;

/**
 * @author Rain
 */
public class LocalTimeToLongConvert extends StdConverter<LocalTime, Long> {

    @Override
    public Long convert(LocalTime value) {
        return LocalDateTimeUtil.localTimeToEpochMilli(value);
    }

}
