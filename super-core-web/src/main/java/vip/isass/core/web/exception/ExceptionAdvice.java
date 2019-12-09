package vip.isass.core.web.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.isass.core.exception.IExceptionMapping;
import vip.isass.core.exception.UnifiedException;
import vip.isass.core.exception.code.IStatusMessage;
import vip.isass.core.exception.code.StatusMessageEnum;
import vip.isass.core.web.Resp;

import javax.annotation.Resource;
import java.util.List;


/**
 * 所有异常转换成 Resp
 *
 * @author Rain
 */
@RestControllerAdvice
public class ExceptionAdvice {

    @Resource
    private List<IExceptionMapping> exceptionMappings;

    /**
     * 处理 controller 抛出的异常
     */
    @ExceptionHandler(Exception.class)
    private Resp<?> exceptionHandler(Exception e) {
        if (e instanceof UnifiedException) {
            UnifiedException exception = (UnifiedException) e;
            return new Resp<>()
                    .setSuccess(false)
                    .setStatus(exception.getStatus())
                    .setMessage(exception.getMsg());
        }

        Class<? extends Exception> aClass = e.getClass();
        for (IExceptionMapping exceptionMapping : exceptionMappings) {
            IStatusMessage statusMessage = exceptionMapping.getStatusCode(aClass);
            if (statusMessage != null) {
                return new Resp<>()
                        .setSuccess(false)
                        .setStatus(statusMessage.getStatus())
                        .setMessage(statusMessage.getMsg() + ((e.getMessage() == null) ? "" : (": " + e.getMessage())));
            }
        }

        return new Resp<>()
                .setSuccess(false)
                .setStatus(StatusMessageEnum.UNDEFINED.getStatus())
                .setMessage(e.getClass().getSimpleName() + ((e.getMessage() == null) ? "" : (": " + e.getMessage())));
    }

}
