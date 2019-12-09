package vip.isass.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import vip.isass.core.support.LocalDateTimeUtil;

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
