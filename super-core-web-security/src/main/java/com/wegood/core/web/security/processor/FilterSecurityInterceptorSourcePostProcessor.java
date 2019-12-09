package com.wegood.core.web.security.processor;

import com.wegood.core.web.security.metadata.SecurityMetadataSource;
import com.wegood.core.web.security.metadata.SecurityMetadataSourceProviderManager;
import com.wegood.core.web.uri.UriPrefixProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * @author Rain
 */
public class FilterSecurityInterceptorSourcePostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private SecurityMetadataSourceProviderManager securityMetadataSourceProviderManager;

    private UriPrefixProvider prefixProvider;

    private List<String> permitUrls;

    public FilterSecurityInterceptorSourcePostProcessor(
        RequestMappingHandlerMapping requestMappingHandlerMapping,
        SecurityMetadataSourceProviderManager securityMetadataSourceProviderManager,
        UriPrefixProvider prefixProvider,
        List<String> permitUrls) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.securityMetadataSourceProviderManager = securityMetadataSourceProviderManager;
        this.prefixProvider = prefixProvider;
        this.permitUrls = permitUrls;
    }

    @Override
    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
        FilterInvocationSecurityMetadataSource securityMetadataSource =
            new SecurityMetadataSource(
                requestMappingHandlerMapping,
                object.getSecurityMetadataSource(),
                securityMetadataSourceProviderManager,
                prefixProvider,
                permitUrls);
        object.setSecurityMetadataSource(securityMetadataSource);

        return object;
    }

}
