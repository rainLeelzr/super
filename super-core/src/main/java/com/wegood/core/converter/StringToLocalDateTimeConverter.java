package com.wegood.core.converter;

import cn.hutool.core.date.format.FastDateFormat;
import com.wegood.core.support.LocalDateTimeUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Rain
 */
@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final String FORMAT = "yyyy/M/dd HH:mm";

    private static final FastDateFormat SDF = FastDateFormat.getInstance(FORMAT);

    @Override
    public LocalDateTime convert(String source) {
        return convert0(source);
    }

    public static LocalDateTime convert0(String source) {
        Long convert = StringDateToMillisConverter.convert0(source);
        return LocalDateTimeUtil.epochMilliToLocalDateTime(convert);
    }

}
