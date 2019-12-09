package vip.isass.core.web.security.authentication;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import vip.isass.core.login.LoginUser;
import vip.isass.core.web.security.authentication.jwt.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rain
 */
public class LoginUserTokenWrapper extends AbstractAuthenticationToken implements LoginUser {

    @Getter
    private List<LoginUser> loginUsers;

    public LoginUserTokenWrapper(LoginUser loginUser) {
        super(null);
        this.loginUsers = CollUtil.newArrayList(loginUser);
        setAuthenticated(false);
    }

    public LoginUserTokenWrapper(LoginUser loginUser, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.loginUsers = CollUtil.newArrayList(loginUser);
        super.setAuthenticated(true);
    }

    public LoginUserTokenWrapper(List<LoginUser> loginUsers, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.loginUsers = loginUsers;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getPrincipal() {
        if (loginUsers == null) {
            return null;
        }
        return loginUsers.stream().map(LoginUser::getUserId).collect(Collectors.joining(","));
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        loginUsers = null;
    }

    @Override
    public String getUserId() {
        return loginUsers == null ? null : loginUsers.stream()
            .filter(l -> JwtAuthenticationToken.class.getSimpleName().equals(l.getTokenFrom()))
            .map(LoginUser::getUserId)
            .findFirst()
            .orElse(null);
    }

    @Override
    public String getAllUserId() {
        return loginUsers == null ? null : loginUsers.stream().map(LoginUser::getUserId).collect(Collectors.joining(","));
    }

    @Override
    public String getNickName() {
        return loginUsers == null ? null : loginUsers.stream()
            .filter(l -> JwtAuthenticationToken.class.getSimpleName().equals(l.getTokenFrom()))
            .map(LoginUser::getNickName)
            .findFirst()
            .orElse(null);
    }

    @Override
    public String getTokenFrom() {
        return loginUsers == null ? null : loginUsers.stream().map(LoginUser::getTokenFrom).collect(Collectors.joining(","));
    }

    public String getAllNickName() {
        return loginUsers == null ? null : loginUsers.stream().map(LoginUser::getNickName).collect(Collectors.joining(","));
    }


    @Override
    public String getLoginFrom() {
        return loginUsers == null ? null : loginUsers.stream().map(LoginUser::getLoginFrom).collect(Collectors.joining(","));
    }

    @Override
    public Integer getVersion() {
        return loginUsers == null ? null : loginUsers.stream().map(LoginUser::getVersion).filter(Objects::nonNull).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return new StringBuilder("{")
            .append("\"userId\":\"")
            .append(getAllUserId()).append('\"')
            .append(",\"nickName\":\"")
            .append(getAllNickName()).append('\"')
            .append(",\"loginFrom\":\"")
            .append(getLoginFrom()).append('\"')
            .append('}')
            .toString();
    }

}
