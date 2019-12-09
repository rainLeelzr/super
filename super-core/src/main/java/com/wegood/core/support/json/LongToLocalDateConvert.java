package com.wegood.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.wegood.core.support.LocalDateTimeUtil;

import java.time.LocalDate;

/**
 * @author Rain
 */
public class LongToLocalDateConvert extends StdConverter<Long, LocalDate> {

    @Override
    public LocalDate convert(Long value) {
        return LocalDateTimeUtil.epochMilliToLocalDate(value);
    }

}
