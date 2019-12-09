package vip.isass.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import vip.isass.core.support.LocalDateTimeUtil;

import java.time.LocalDate;

/**
 * @author Rain
 */
public class LocalDateToLongConvert extends StdConverter<LocalDate, Long> {

    @Override
    public Long convert(LocalDate value) {
        return LocalDateTimeUtil.localDateToEpochMilli(value);
    }

}
