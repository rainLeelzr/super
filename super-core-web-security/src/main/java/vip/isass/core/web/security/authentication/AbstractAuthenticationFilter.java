package vip.isass.core.web.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import vip.isass.core.login.DefaultLoginUser;
import vip.isass.core.login.LoginUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Rain
 */
@Slf4j
public abstract class AbstractAuthenticationFilter extends BasicAuthenticationFilter {

    public AbstractAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    protected void saveAuthentication(DefaultLoginUser defaultLoginUser, Collection<GrantedAuthority> authorities) {
        // 已有授权信息
        LoginUserTokenWrapper tokenWrapper = (LoginUserTokenWrapper) SecurityContextHolder.getContext().getAuthentication();


        if (tokenWrapper == null) {
            tokenWrapper = new LoginUserTokenWrapper(defaultLoginUser, authorities);
        } else {
            // loginUsers
            List<LoginUser> loginUsers = tokenWrapper.getLoginUsers();
            loginUsers.add(defaultLoginUser);

            // newAuthorities
            Collection<GrantedAuthority> newAuthorities;
            if (tokenWrapper.getAuthorities().isEmpty()) {
                newAuthorities = authorities;
            } else {
                newAuthorities = new ArrayList<>(tokenWrapper.getAuthorities().size() + authorities.size());
                newAuthorities.addAll(tokenWrapper.getAuthorities());
                newAuthorities.addAll(authorities);
            }

            // token
            tokenWrapper = new LoginUserTokenWrapper(loginUsers, newAuthorities);
        }

        SecurityContextHolder.getContext().setAuthentication(tokenWrapper);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof LoginUserTokenWrapper) {
            LoginUserTokenWrapper loginUserAuthenticationToken = (LoginUserTokenWrapper) authentication;
            log.debug("认证成功[{}], 正在访问[{} {}], userId[{}], name[{}], 拥有角色{}",
                this.getClass().getSimpleName(),
                request.getMethod(),
                request.getRequestURL(),
                loginUserAuthenticationToken.getAllUserId(),
                loginUserAuthenticationToken.getAllNickName(),
                authentication.getAuthorities());
        }
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.debug("认证失败[{}]，正在访问 {} {}", this.getClass().getSimpleName(), request.getMethod(), request.getRequestURL());
    }

}
