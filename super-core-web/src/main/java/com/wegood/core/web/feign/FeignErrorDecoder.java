package com.wegood.core.web.feign;

import com.wegood.core.exception.UnifiedException;
import com.wegood.core.exception.code.StatusMessageEnum;
import com.wegood.core.support.JsonUtil;
import com.wegood.core.web.Resp;
import cn.hutool.core.io.IoUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Rain
 */
@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try (Reader reader = response.body().asReader()) {
            String read = IoUtil.read(reader);
            log.error("feign请求异常：{}, {}", methodKey, read);
            Resp resp = JsonUtil.DEFAULT_INSTANCE.readValue(read, Resp.class);
            if (resp.getSuccess() == null
                && resp.getMessage() == null
                && resp.getData() == null) {
                return new UnifiedException(StatusMessageEnum.FAIL, read);
            }
            return new UnifiedException(resp.getStatus(), resp.getMessage());
        } catch (IOException e) {
            return new UnifiedException(StatusMessageEnum.FEIGN_ERROR, StatusMessageEnum.FEIGN_ERROR.getMsg() + " " + methodKey + " " + e.getMessage());
        }
    }

}
