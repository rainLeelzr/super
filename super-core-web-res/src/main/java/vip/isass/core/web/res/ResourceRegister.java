package vip.isass.core.web.res;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import vip.isass.core.web.uri.UriPrefixProvider;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rain
 */
@Slf4j
@Component
public class ResourceRegister implements InitializingBean {

    @javax.annotation.Resource
    private RequestMappingHandlerMapping handlerMapping;

    @javax.annotation.Resource
    private ResRegister resRegister;

    @javax.annotation.Resource
    private UriPrefixProvider uriPrefixProvider;

    private boolean init = false;

    public void register() {
        // 必须要设置了applicationName，才给注册
        if (StrUtil.isBlank(uriPrefixProvider.getUriPrefix())) {
            log.info("未设置applicationName, 跳过 res 注册流程");
            return;
        }

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        List<Resource> collect = handlerMethods
            .entrySet()
            .parallelStream()
            .map(h -> {
                    RequestMappingInfo mappingInfo = h.getKey();

                    RequestMethodsRequestCondition methodsCondition = mappingInfo.getMethodsCondition();
                    PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();

                    Set<Resource> resources = new HashSet<>(4);
                    for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                        for (String uri : patternsCondition.getPatterns()) {
                            resources.add(new Resource()
                                .setHttpMethod(requestMethod.name())
                                .setTransportProtocol(Resource.TransportProtocol.HTTP)
                                .setUri(uriPrefixProvider.getUriPrefix() + uri.trim()));
                        }
                    }
                    return resources;
                }
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if (CollUtil.isEmpty(collect)) {
            return;
        }

        try {
            List<Resource> allRegisteredResource = resRegister.getAllRegisteredResourceByPrefixUri(uriPrefixProvider.getUriPrefix());
            if (CollUtil.isNotEmpty(allRegisteredResource)) {
                collect = collect.stream()
                    .filter(r -> !this.isExist(r, allRegisteredResource))
                    .collect(Collectors.toList());
            }

            if (CollUtil.isNotEmpty(collect)) {
                resRegister.register(collect);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 判断一个资源是否已存在集合中
     */
    private boolean isExist(Resource resource, Collection<Resource> resources) {
        if (resource == null || CollUtil.isEmpty(resources)) {
            return false;
        }

        for (Resource temp : resources) {
            // 如果传输协议不相同，则执行下一次循环
            if (resource.getTransportProtocol() != temp.getTransportProtocol()) {
                continue;
            }

            // 判断 httpMethod
            if (!(StrUtil.isNotBlank(resource.getHttpMethod()) && StrUtil.isNotBlank(temp.getHttpMethod()))) {
                continue;
            }
            if (resource.getHttpMethod() == null) {
                continue;
            }
            if (!resource.getHttpMethod().equals(temp.getHttpMethod())) {
                continue;
            }

            // 判断 uri
            if (!(StrUtil.isNotBlank(resource.getUri()) && StrUtil.isNotBlank(temp.getUri()))) {
                continue;
            }
            if (resource.getUri() == null) {
                continue;
            }
            if (!resource.getUri().equals(temp.getUri())) {
                continue;
            }

            return true;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() {
        if (!init) {
            init = true;
            register();
        }
    }
}
