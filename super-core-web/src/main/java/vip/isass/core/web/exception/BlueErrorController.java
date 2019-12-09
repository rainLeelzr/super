package vip.isass.core.web.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import vip.isass.core.exception.IStatusMapping;
import vip.isass.core.exception.UnifiedException;
import vip.isass.core.exception.code.IStatusMessage;
import vip.isass.core.exception.code.StatusMessageEnum;
import vip.isass.core.web.Resp;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Rain
 */
@Controller
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class BlueErrorController implements ErrorController {

    private static final String PATH = "/error";

    private final ErrorAttributes errorAttributes;

    @Resource
    private List<IStatusMapping> statusMappings;

    @Autowired
    public BlueErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    /**
     * 处理还没进入 controller 就抛出的异常
     */
    @RequestMapping(value = PATH)
    @ResponseBody
    public Resp<?> errorJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, true);
        Object exception = errorAttributes.get(RequestDispatcher.ERROR_EXCEPTION);
        if (exception instanceof UnifiedException) {
            return new Resp<>()
                .setSuccess(Boolean.FALSE)
                .setStatus(((UnifiedException) exception).getStatus())
                .setMessage(((UnifiedException) exception).getMsg());
        }

        Integer status = Integer.valueOf(errorAttributes.get("status").toString());
        for (IStatusMapping statusMapping : statusMappings) {
            IStatusMessage statusCode = statusMapping.getErrorCode(status);
            if (statusCode != null) {
                return new Resp<>()
                    .setSuccess(false)
                    .setStatus(statusCode.getStatus())
                    .setMessage(statusCode.getMsg() + ": " + request.getMethod() + " "
                        + errorAttributes.get("path") + " "
                        + errorAttributes.get("error") + " "
                        + errorAttributes.get("exception") + " "
                        + errorAttributes.get("message"));
            }
        }

        return new Resp<>()
            .setSuccess(false)
            .setStatus(status)
            .setMessage(StatusMessageEnum.UNDEFINED.getMsg() + " " + request.getMethod() + " " + errorAttributes.get("path") + " " + errorAttributes.get("error"));
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, includeStackTrace);
        Throwable error = this.errorAttributes.getError(servletWebRequest);
        if (error != null) {
            errorAttributes.put(RequestDispatcher.ERROR_EXCEPTION, error);
        }
        return errorAttributes;
    }

}
