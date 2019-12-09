package vip.isass.core.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import vip.isass.core.entity.Json;
import vip.isass.core.exception.UnifiedException;
import vip.isass.core.exception.code.StatusMessageEnum;

/**
 * @author Rain
 */
@Component
public class StringToJsonConverter implements Converter<Object, Json> {

    @Autowired(required = false)
    private Json json;

    @Override
    public Json convert(Object source) {
        if (json == null) {
            throw new UnsupportedOperationException("当前环境没有json实现");
        }
        try {
            Json temp = this.json.getClass().newInstance();
            return temp.fromObject(source);
        } catch (Exception e) {
            throw new UnifiedException(StatusMessageEnum.FAIL, "反序列化jsonb字段错误：{}" + e.getMessage());
        }
    }

}
