package com.wegood.core.web.security;

import com.wegood.core.login.LoginUser;
import com.wegood.core.login.LoginUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Rain
 */
@Service
public class LoginUserServiceImpl implements LoginUserService {

    @Override
    public LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        if (authentication instanceof LoginUser) {
            return (LoginUser) authentication;
        }

        return null;
    }
}
