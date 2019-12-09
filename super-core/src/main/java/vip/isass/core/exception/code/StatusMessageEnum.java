package vip.isass.core.exception.code;

import lombok.Getter;

/**
 * @author Rain
 */
@Getter
public enum StatusMessageEnum implements IStatusMessage {

    UNDEFINED(-2, "未定义错误"),
    FAIL(-1, "操作失败"),
    SUCCESS(1, "操作成功"),

    DB_INSERT_FAIL(10, "插入数据库失败"),
    TOO_MANY_RESULT(11, "数据重复"),
    ALREADY_PRESENT(12, "数据已存在"),
    ABSENT(13, "数据不存在"),
    UN_SUPPORT_OPERATION(14, "不支持该操作"),
    JWT_TOKEN_ERROR(15, "token错误或过期"),
    UN_LOGIN(16, "未登录系统"),
    DUPLICATE_KEY(17, "key重复或冲突"),
    FEIGN_ERROR(18, "feign请求异常"),
    TOKEN_FORCE_OFFLINE(19, "强制下线"),
    VERIFICATION_CODE_ALREADY_SEND(20, "请勿重复发送验证码"),

    ACCESS_DENIED_403(403, "权限不足"),
    NOT_FOUND_404(404, "链接不存在"),
    METHOD_NOT_ALLOWED_405(405, "不支持的HTTP方法"),
    INTERNAL_SERVER_ERROR_500(500, "服务器内部错误"),

    // 用户名密码错误,
    USERNAME_PASSWORD_ERROR(1001, "用户名或密码错误"),
    ILLEGAL_ARGUMENT_ERROR(1002, "参数错误"),
    MOBILE_ABSENT(1003, "手机未注册"),

    URI_PARSE_ERROR(1003, "uri解析错误"),
    HTTP_METHOD_PARSE_ERROR(1004, "http method 解析错误"),
    MOBILE_PRESENT(1005, "手机用户已存在"),
    VERIFICATION_CODE_ERROR(1006, "验证码错误");

    private Integer status;

    private String msg;

    StatusMessageEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private StatusMessageEnum setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public static StatusMessageEnum getByCode(Integer status) {
        for (StatusMessageEnum errorCode : values()) {
            if (errorCode.status.equals(status)) {
                return errorCode;
            }
        }
        return null;
    }

}
