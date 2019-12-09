package com.wegood.core.web.security.authentication.ms;

import cn.hutool.core.util.StrUtil;
import com.wegood.core.login.DefaultLoginUser;
import com.wegood.core.web.security.authentication.AbstractAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Rain
 */
@Slf4j
public class MsAuthenticationFilter extends AbstractAuthenticationFilter {

    public MsAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String value = request.getHeader(MsAuthenticationHeaderProvider.HEADER);

        if (StrUtil.isBlank(value)) {
            chain.doFilter(request, response);
            return;
        }

        value = value.trim();

        try {
            Authentication authRequest = new MsAuthenticationToken(value);
            MsAuthenticationToken authResult = (MsAuthenticationToken) getAuthenticationManager().authenticate(authRequest);

            DefaultLoginUser defaultLoginUser = new DefaultLoginUser()
                .setUserId(authResult.getAppName())
                .setNickName(authResult.getAppName())
                .setLoginFrom(authResult.getAppName())
                .setVersion(1)
                .setTokenFrom(MsAuthenticationToken.class.getSimpleName());

            saveAuthentication(defaultLoginUser, authResult.getAuthorities());
            onSuccessfulAuthentication(request, response, authResult);
        } catch (AuthenticationException failed) {
            onUnsuccessfulAuthentication(request, response, failed);
        }

        chain.doFilter(request, response);
    }

}
