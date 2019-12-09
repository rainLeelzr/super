package com.wegood.core.web.security.authentication.jwt;

import com.wegood.core.login.DefaultLoginUser;
import com.wegood.core.web.security.authentication.AbstractAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Rain
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationFilter {

    private JwtCacheService jwtCacheService;

    private EndsConfiguration endsConfiguration;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtCacheService jwtCacheService,
                                   EndsConfiguration endsConfiguration) {
        super(authenticationManager);
        this.jwtCacheService = jwtCacheService;
        this.endsConfiguration = endsConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(JwtUtil.PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = header.replace(JwtUtil.PREFIX, "");
            JwtAuthenticationToken authResult = (JwtAuthenticationToken) getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));

            JwtClaim jwtClaim = authResult.getJwtClaim();
            DefaultLoginUser defaultLoginUser = new DefaultLoginUser()
                .setUserId(jwtClaim.getUid())
                .setNickName(jwtClaim.getName())
                .setLoginFrom(jwtClaim.getFr())
                .setVersion(jwtClaim.getV())
                .setTokenFrom(JwtAuthenticationToken.class.getSimpleName());

            // 处理多端登录/注销账号的情况
            if (!processEnds(defaultLoginUser)) {
                throw new CredentialsExpiredException("强制下线");
            }

            // 保存已验证的权限信息
            saveAuthentication(defaultLoginUser, authResult.getAuthorities());

            // 权限认证成功方法
            onSuccessfulAuthentication(request, response, authResult);
        } catch (AuthenticationException failed) {
            onUnsuccessfulAuthentication(request, response, failed);
        }

        chain.doFilter(request, response);
    }

    private boolean processEnds(DefaultLoginUser defaultLoginUser) {
        // 是否强制下线
        Integer forceOfflineVersion = jwtCacheService.getForceOfflineVersion(defaultLoginUser.getUserId());
        if (forceOfflineVersion != null && defaultLoginUser.getVersion() <= forceOfflineVersion) {
            // throw new UnifiedException(StatusMessageEnum.TOKEN_FORCE_OFFLINE);
            return false;
        }

        // 检查多端登录
        return checkEndsOnline(defaultLoginUser);
    }

    private boolean checkEndsOnline(DefaultLoginUser defaultLoginUser) {
        // 如果允许所有端同时在线，则跳过校验
        if (endsConfiguration.isAllEndsSameTimeOnline()) {
            return true;
        }

        // 是否属于直接在线终端
        if (endsConfiguration.isDirectOnlineEnds(defaultLoginUser.getLoginFrom())) {
            // 是否同端多登
            if (endsConfiguration.isSameEndsOnline(defaultLoginUser.getLoginFrom())) {
                return true;
            } else {
                // 如果已有同端更大的版本号已经登录，则此token无效
                Integer maxVersionOfEnd = jwtCacheService.getVersionByEnd(defaultLoginUser.getUserId(), defaultLoginUser.getLoginFrom());
                if (maxVersionOfEnd == null) {
                    // 没有终端记录，即此 token 是启用多端验证前生成的，需重新登录
                    // throw new UnifiedException(StatusMessageEnum.TOKEN_FORCE_OFFLINE);
                    return false;
                } else if (maxVersionOfEnd > defaultLoginUser.getVersion()) {
                    // throw new UnifiedException(StatusMessageEnum.TOKEN_FORCE_OFFLINE);
                    return false;
                }
            }
        }

        // 是否属于互斥终端
        if (endsConfiguration.isMutexEnds(defaultLoginUser.getLoginFrom())) {
            Map<String, Integer> versions = jwtCacheService.getVersionByEnds(defaultLoginUser.getUserId(), endsConfiguration.getMutexEnds());
            // 找出互斥终端中最高版本的终端
            String maxEnd = null;
            Integer maxVersion = null;
            for (Map.Entry<String, Integer> entry : versions.entrySet()) {
                if (maxEnd == null) {
                    maxEnd = entry.getKey();
                    maxVersion = entry.getValue();
                    continue;
                }

                if (entry.getValue() == null) {
                    continue;
                }

                if (maxVersion >= entry.getValue()) {
                    continue;
                }

                maxEnd = entry.getKey();
                maxVersion = entry.getValue();
            }
            if (maxEnd == null) {
                // 没有终端记录，即此 token 是启用多端验证前生成的，则强制下线
                // throw new UnifiedException(StatusMessageEnum.TOKEN_FORCE_OFFLINE);
                return false;
            }

            // 如果最大版本的终端，不是此次 token 的终端，则强制下线
            if (!maxEnd.equals(defaultLoginUser.getLoginFrom())) {
                // throw new UnifiedException(StatusMessageEnum.TOKEN_FORCE_OFFLINE);
                return false;
            }

            // 是否同端多登
            if (endsConfiguration.isSameEndsOnline(defaultLoginUser.getLoginFrom())) {
                return true;
            } else {
                return maxVersion.equals(defaultLoginUser.getVersion());
            }
        }

        // 是否同端多登
        return endsConfiguration.isSameEndsOnline(defaultLoginUser.getLoginFrom());

        // 其他没配置的终端，强制下线
        // throw new UnifiedException(StatusMessageEnum.TOKEN_FORCE_OFFLINE);
    }

}
