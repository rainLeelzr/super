package vip.isass.core.web;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import vip.isass.core.exception.UnifiedException;
import vip.isass.core.exception.code.StatusMessageEnum;

import java.util.Collections;
import java.util.List;

/**
 * 方法、接口调用的返回结果
 *
 * @author Rain
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class Resp<T> {

    /**
     * 是否成功常
     */
    @JsonInclude
    private Boolean success;

    /**
     * 状态码
     */
    @JsonInclude
    private int status;

    /**
     * 描述信息
     */
    @JsonInclude
    private String message;

    /**
     * 调用接口得到的数据
     */
    @JsonInclude
    private T data;

    public static <T> Resp<T> bizSuccess(T data) {
        return new Resp<T>()
            .setSuccess(Boolean.TRUE)
            .setStatus(StatusMessageEnum.SUCCESS.getStatus())
            .setMessage(StatusMessageEnum.SUCCESS.getMsg())
            .setData(data);
    }

    public static <T> Resp<T> bizSuccess() {
        return new Resp<T>()
            .setSuccess(Boolean.TRUE)
            .setStatus(StatusMessageEnum.SUCCESS.getStatus())
            .setMessage(StatusMessageEnum.SUCCESS.getMsg());
    }

    public static Resp<List<String>> bizSuccessEmptyStringList() {
        return new Resp<List<String>>()
            .setSuccess(Boolean.TRUE)
            .setStatus(StatusMessageEnum.SUCCESS.getStatus())
            .setMessage(StatusMessageEnum.SUCCESS.getMsg())
            .setData(Collections.emptyList());
    }

    public static <T> Resp<T> bizFail(T data) {
        return new Resp<T>()
            .setSuccess(Boolean.FALSE)
            .setStatus(StatusMessageEnum.FAIL.getStatus())
            .setMessage(StatusMessageEnum.FAIL.getMsg())
            .setData(data);
    }

    public static <T> Resp<T> bizFail() {
        return new Resp<T>()
            .setSuccess(Boolean.FALSE)
            .setStatus(StatusMessageEnum.FAIL.getStatus())
            .setMessage(StatusMessageEnum.FAIL.getMsg());
    }

    public void exceptionIfUnSuccess() {
        if (!this.success) {
            throw new UnifiedException(this.status, this.message);
        }
    }

    public T dataIfSuccessOrException() {
        if (this.getSuccess()) {
            return this.getData();
        }
        throw new UnifiedException(this.getStatus(), this.getMessage());
    }

    public T dataIfSuccessOrWarn() {
        if (!this.getSuccess()) {
            log.warn("{}", this.getMessage());
        }
        return this.getData();
    }

    public T dataIfSuccess() {
        if (this.getSuccess()) {
            return this.getData();
        }
        return null;
    }

    public T notNullDataOrException() {
        exceptionIfUnSuccess();
        Assert.notNull(this.getData(), "data为null");
        return this.getData();
    }

    public T notNullDataOrException(String message, String... args) {
        exceptionIfUnSuccess();
        if (this.getData() == null) {
            throw new UnifiedException(StatusMessageEnum.ABSENT, StrUtil.format(message, args));
        }
        return this.getData();
    }

    @Override
    public String toString() {
        return new StringBuilder("{")
            .append("\"success\":")
            .append(success)
            .append(",\"status\":")
            .append(status)
            .append(",\"message\":\"")
            .append(message).append('\"')
            .append(",\"data\":")
            .append(data)
            .append('}')
            .toString();
    }
}
