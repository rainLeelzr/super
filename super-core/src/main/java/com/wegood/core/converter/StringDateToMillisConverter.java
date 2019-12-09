package com.wegood.core.converter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Rain
 */
@Component
public class StringDateToMillisConverter implements Converter<String, Long> {

    private static final String FORMAT = "yyyy/M/dd HH:mm";

    private static final FastDateFormat SDF = FastDateFormat.getInstance(FORMAT);

    @Override
    public Long convert(String source) {
        return convert0(source);
    }

    public static Long convert0(String source) {
        if (StrUtil.isBlank(source)) {
            return null;
        }

        try {
            return Long.parseLong(source);
        } catch (NumberFormatException e) {
            // do nothing
        }

        try {
            return DateUtil.parse(source).getTime();
        } catch (Exception e) {
            // do nothing
        }

        if (source.length() == FORMAT.length() || source.length() == FORMAT.length() + 1) {
            return DateUtil.parse(source, SDF).getTime();
        }

        throw new IllegalArgumentException("StringDateToLong 转换失败：" + source);
    }

}
