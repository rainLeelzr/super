package vip.isass.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import vip.isass.core.support.LocalDateTimeUtil;

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
