package vip.isass.core.web.security.metadata;

import java.util.Collection;

/**
 * @author Rain
 */
public interface SecurityMetadataSourceProvider {

    /**
     * 获取指定用户拥有的角色
     */
    Collection<String> findRoleCodesByUserId(String userId);

    /**
     * 获取访问指定 uri 需要的角色
     */
    Collection<String> findRoleCodesByUri(String uri, String method);

}
