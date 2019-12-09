package vip.isass.core.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vip.isass.core.login.LoginUser;
import vip.isass.core.login.LoginUserService;

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
