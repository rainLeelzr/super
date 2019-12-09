package com.wegood.core.web.security.metadata;

import com.wegood.core.support.FunctionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
public class SecurityMetadataSourceProviderManager {

    @Resource
    private List<SecurityMetadataSourceProvider> providers;

    public Collection<String> findRolesByUserId(String userId) {
        return FunctionUtil.getFirstNotNullValueFromCollection(providers, service -> service.findRoleCodesByUserId(userId));
    }

    public Collection<String> findRolesByUri(String uri, String method) {
        return FunctionUtil.getFirstNotNullValueFromCollection(providers, service -> service.findRoleCodesByUri(uri, method));
    }

}
