/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.isass.core.web.security.authentication.ms;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Rain
 */
public class MsAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private String tokenValue;

    @Getter
    private String appName;

    private String secret;

    public MsAuthenticationToken(String tokenValue) {
        super(null);
        this.tokenValue = tokenValue;
    }

    public MsAuthenticationToken(String appName, String secret, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.appName = appName;
        this.secret = secret;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return secret;
    }

    @Override
    public Object getPrincipal() {
        return appName;
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
        appName = null;
        secret = null;
    }

}
