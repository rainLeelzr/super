package vip.isass.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import vip.isass.core.support.LocalDateTimeUtil;

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
