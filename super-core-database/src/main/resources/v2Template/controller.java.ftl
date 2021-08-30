<#include "./segment/copyright.ftl">

package ${cfg.controllerPackageName};

import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ${cfg.criteriaPackageName}.${entity}Criteria;
import ${cfg.entityPackageName}.${entity};
import ${cfg.servicePackageName}.v2.${table.serviceName};
import ${cfg.servicePackageName}.${table.serviceImplName};
import vip.isass.core.web.structure.IV2Controller;

import javax.annotation.Resource;

/**
 * <p>
 * <#if table.comment??>${table.comment}<#else>${table.name}</#if> controller
 * </p>
 *
 * @author ${author}
 */
@Slf4j
@RestController
@Api(tags = "v2<#if table.comment??>${table.comment!}<#else>${entity}</#if>")
@RequestMapping(${table.serviceName}.URI_FIRST_PART)
public class ${table.controllerName} implements ${table.serviceName}, IV2Controller<${entity}> {

    @Getter
    @Resource
    private ${table.serviceImplName} service;

}
