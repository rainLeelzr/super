package vip.isass.core.support.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import vip.isass.core.entity.Json;
import vip.isass.core.exception.UnifiedException;
import vip.isass.core.exception.code.StatusMessageEnum;
import vip.isass.core.support.SpringContextUtil;
import vip.isass.core.exception.UnifiedException;
import vip.isass.core.exception.code.StatusMessageEnum;

/**
 * @author Rain
 */
public class ObjectToJsonConvert extends StdConverter<Object, Json> {

    private static Json json;

    @Override
    public Json convert(Object object) {
        if (json == null) {
            try {
                json = SpringContextUtil.getBean(Json.class);
            } catch (Exception e) {
                // ignore
            }
        }
        // if (json == null) {
        //     throw new UnsupportedOperationException("spring环境未初始化完成，或当前环境没有json实现");
        // }

        try {
            Json temp;
            if (json == null) {
                temp = DefaultJson.class.newInstance();
            } else {
                temp = json.getClass().newInstance();
            }
            return temp.fromObject(object);
        } catch (Exception e) {
            throw new UnifiedException(StatusMessageEnum.FAIL, "反序列化jsonb字段错误：{}" + e.getMessage());
        }

    }

}
