package com.wegood.core.web.security.metadata;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wegood.core.exception.UnifiedException;
import com.wegood.core.exception.code.StatusMessageEnum;
import com.wegood.core.web.security.SecurityConst;
import com.wegood.core.web.uri.UriPrefixProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Rain
 */
@Slf4j
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    private SecurityMetadataSourceProviderManager securityMetadataSourceProviderManager;

    private List<String> permitUrls;

    private UriPrefixProvider uriPrefixProvider;

    private static final List<String> IGNORE_LOGGING_URI = CollUtil.newArrayList("/error");

    /**
     * Sets the internal request map from the supplied map. The key elements should be of
     * type {@link RequestMatcher}, which. The contextPath stored in the key will depend on the
     * type of the supplied UrlMatcher.
     */
    public SecurityMetadataSource(RequestMappingHandlerMapping requestMappingHandlerMapping,
                                  FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource,
                                  SecurityMetadataSourceProviderManager securityMetadataSourceProviderManager,
                                  UriPrefixProvider uriPrefixProvider,
                                  List<String> permitUrls) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.filterInvocationSecurityMetadataSource = filterInvocationSecurityMetadataSource;
        this.securityMetadataSourceProviderManager = securityMetadataSourceProviderManager;
        this.uriPrefixProvider = uriPrefixProvider;
        this.permitUrls = permitUrls;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return filterInvocationSecurityMetadataSource.getAllConfigAttributes();
    }

    /**
     * 获取访问该资源所需要的权限
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        HttpServletRequest request = getRequest(object);
        Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoAndHandlerMethodEntry = getRequestMappingInfoAndHandlerMethodEntry(request);
        if (requestMappingInfoAndHandlerMethodEntry == null) {
            if (!permitUrls.contains(request.getRequestURI())) {
                log.trace("解析不到uri对应的方法：{}", request.getRequestURI());
            }
            return Collections.emptyList();
        }

        String httpMethod = request.getMethod();

        Collection<ConfigAttribute> attributes = null;
        String mappingUri = getMappingUri(requestMappingInfoAndHandlerMethodEntry, object);

        HandlerMethod handlerMethod = requestMappingInfoAndHandlerMethodEntry.getValue();

        Collection<String> roleCodes = securityMetadataSourceProviderManager.findRolesByUri(
            uriPrefixProvider.getUriPrefix() + mappingUri,
            httpMethod);

        if (CollUtil.isNotEmpty(roleCodes)) {
            attributes = roleCodes.stream()
                .filter(Objects::nonNull)
                .filter(StrUtil::isNotBlank)
                .map(SecurityConfig::new)
                .collect(Collectors.toList());
        }

        if (attributes == null) {
            attributes = new HashSet<>(16);
        }

        attributes.addAll(SecurityConst.CONFIG_ATTRIBUTES);

        Collection<ConfigAttribute> originalAttributes = filterInvocationSecurityMetadataSource.getAttributes(object);
        CollUtil.addAll(attributes, originalAttributes == null ? Collections.emptyList() : originalAttributes);

        if (!IGNORE_LOGGING_URI.contains(request.getRequestURI())) {
            log.debug("访问 {} {} 所需权限：{}", httpMethod, request.getRequestURI(), attributes);
        }
        return attributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    private String getMappingUri(Map.Entry<RequestMappingInfo, HandlerMethod> entry, Object object) {
        if (entry != null) {
            return entry.getKey().getPatternsCondition().getPatterns().iterator().next().trim();
        }

        if (object instanceof FilterInvocation) {
            FilterInvocation invocation = (FilterInvocation) object;
            return invocation.getRequestUrl();
        }

        throw new UnifiedException(StatusMessageEnum.URI_PARSE_ERROR);
    }

    private HttpServletRequest getRequest(Object object) {
        if (object instanceof FilterInvocation) {
            FilterInvocation invocation = (FilterInvocation) object;
            return invocation.getRequest();
        }
        return null;
    }

    private Map.Entry<RequestMappingInfo, HandlerMethod> getRequestMappingInfoAndHandlerMethodEntry(HttpServletRequest request) {
        HandlerExecutionChain handlerExecutionChain;
        try {
            handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
        } catch (Exception e) {
            return null;
        }
        if (handlerExecutionChain == null) {
            return null;
        }
        Object handler = handlerExecutionChain.getHandler();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> next : handlerMethods.entrySet()) {
            HandlerMethod value = next.getValue();
            if (value.toString().equals(handler.toString())) {
                return next;
            }
        }
        return null;
    }
}
