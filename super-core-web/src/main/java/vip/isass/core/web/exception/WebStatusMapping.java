package vip.isass.core.web.exception;

import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Component;
import vip.isass.core.exception.IStatusMapping;
import vip.isass.core.exception.code.StatusMessageEnum;

import java.util.Map;

/**
 * @author Rain
 */
@Component
public class WebStatusMapping implements IStatusMapping {

    private static Map<Integer, StatusMessageEnum> statusMapping = MapUtil.<Integer, StatusMessageEnum>builder()
            .put(403, StatusMessageEnum.ACCESS_DENIED_403)
            .put(404, StatusMessageEnum.NOT_FOUND_404)
            .put(405, StatusMessageEnum.METHOD_NOT_ALLOWED_405)
            .put(500, StatusMessageEnum.INTERNAL_SERVER_ERROR_500)
            .build();

    @Override
    public StatusMessageEnum getErrorCode(Integer code) {
        return statusMapping.get(code);
    }

}
