package com.wegood.core.web.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Administrator
 */
public interface UserLoader {

    UserDetails load(String userId);

}
