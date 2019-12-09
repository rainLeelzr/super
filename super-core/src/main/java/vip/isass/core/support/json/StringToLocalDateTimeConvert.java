package vip.isass.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import vip.isass.core.converter.StringToLocalDateTimeConverter;

import java.time.LocalDateTime;

/**
 * @author Rain
 */
public class StringToLocalDateTimeConvert extends StdConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String value) {
        return StringToLocalDateTimeConverter.convert0(value);
    }

}
