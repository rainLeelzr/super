package vip.isass.core.web.security.authentication.ms;

import vip.isass.core.web.security.SecurityConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Rain
 */
@Slf4j
@Component
public class MsAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private MsAuthenticationHeaderProvider msAuthenticationHeaderProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MsAuthenticationToken msAuthenticationToken = (MsAuthenticationToken) authentication;
        int index = msAuthenticationToken.getTokenValue().lastIndexOf(msAuthenticationHeaderProvider.getDotSecret());
        if (index < 1) {
            throw new BadCredentialsException("ms token错误");
        }

        // 获取用户拥有的角色
        Collection<? extends GrantedAuthority> authorities = Stream.of(SecurityConst.ROLE_MS)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new MsAuthenticationToken(
            msAuthenticationToken.getTokenValue().substring(0, index),
            msAuthenticationHeaderProvider.getSecret(),
            authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(MsAuthenticationToken.class);
    }

}
