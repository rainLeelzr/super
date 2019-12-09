package vip.isass.core.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Rain
 */
@Component
public class BlueUserDetailsService implements UserDetailsService {

    @Resource
    private UserLoader userLoader;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDetails userDetails = userLoader.load(userId);
        if (userDetails != null) {
            return userDetails;
        }
        throw new UsernameNotFoundException(userId);
    }

}
