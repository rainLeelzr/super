package vip.isass.core.web;

import vip.isass.core.exception.code.IStatusMessage;
import vip.isass.core.exception.code.StatusMessageEnum;
import lombok.Getter;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author Rain
 */
@Getter
public enum WebStatusCode implements IStatusMessage {

    NOT_FOUND_405(StatusMessageEnum.METHOD_NOT_ALLOWED_405.getStatus(), StatusMessageEnum.METHOD_NOT_ALLOWED_405.getMsg(), HttpRequestMethodNotSupportedException.class),;

    private Integer status;

    private String msg;

    private Class<? extends Exception> exception;

    WebStatusCode(Integer status, String msg, Class<? extends Exception> exception) {
        this.status = status;
        this.msg = msg;
        this.exception = exception;
    }

    public static WebStatusCode getByStatus(Integer status) {
        for (WebStatusCode statusCode : values()) {
            if (statusCode.getStatus().equals(status)) {
                return statusCode;
            }
        }
        return null;
    }


    @Override
    public String getMsg() {
        return msg;
    }
}
