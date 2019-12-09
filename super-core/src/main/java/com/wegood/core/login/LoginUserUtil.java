package com.wegood.core.login;

import com.wegood.core.exception.UnifiedException;
import com.wegood.core.exception.code.StatusMessageEnum;
import com.wegood.core.support.SpringContextUtil;

/**
 * @author Rain
 */
public class LoginUserUtil {

    private static LoginUserService service = null;

    public static LoginUser getLoginUser() {
        if (service == null) {
            if (SpringContextUtil.isInitialized()) {
                try {
                    service = SpringContextUtil.getBean(LoginUserService.class);
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return service == null ? null : service.getLoginUser();
    }

    public static LoginUser getLoginUserOrException() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw new UnifiedException(StatusMessageEnum.UN_LOGIN);
        }
        return loginUser;
    }

    public static void checkLogin() {
        if (getLoginUser() == null) {
            throw new UnifiedException(StatusMessageEnum.UN_LOGIN);
        }
    }
}
