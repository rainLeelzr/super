package vip.isass.core.web.security.authentication.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import vip.isass.core.support.LocalDateTimeUtil;
import vip.isass.core.web.security.metadata.SecurityMetadataSourceProviderManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import vip.isass.core.support.LocalDateTimeUtil;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rain
 */
@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Value("${security.jwt.secret:wegood123456}")
    private String secret;

    @Resource
    private SecurityMetadataSourceProviderManager securityMetadataSourceProviderManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        Claims claims;
        try {
            claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("token");
        } catch (Exception e) {
            throw new BadCredentialsException(token);
        }

        // 判断 token 是否过期
        Date expiration = claims.getExpiration();
        if (expiration.before(LocalDateTimeUtil.nowDate())) {
            throw new BadCredentialsException(token);
        }

        // 判断用户id是否存在


        JwtClaim jwtClaim = new JwtClaim();
        BeanUtil.copyProperties(claims, jwtClaim);

        // 获取用户拥有的角色
        Collection<GrantedAuthority> configAttributes = Collections.emptyList();
        Collection<String> roles = securityMetadataSourceProviderManager.findRolesByUserId(jwtClaim.getUid());
        if (!CollUtil.isEmpty(roles)) {
            configAttributes = roles.stream()
                .filter(Objects::nonNull)
                .filter(StrUtil::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        }

        return new JwtAuthenticationToken(token, jwtClaim, configAttributes);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
