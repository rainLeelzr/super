package vip.isass.core.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Rain
 */
@Component
public class StringToClassConverter implements Converter<String, Class> {

    private static final String PREFIX = "class ";

    @Override
    public Class convert(String source) {
        if (source == null) {
            return null;
        }
        if (source.startsWith(PREFIX)) {
            source = source.replaceFirst("class ", "");
        }
        try {
            return Class.forName(source);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
