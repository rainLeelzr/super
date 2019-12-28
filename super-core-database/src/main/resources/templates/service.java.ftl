<#include "./segment/copyright.ftl">

package ${package.Service};

import org.springframework.stereotype.Service;
import ${cfg.package}.${cfg.moduleName}.db.repository.${entity}MpRepository;
import ${cfg.package}.${cfg.moduleName}.db.${cfg.prefix}.repository.${cfg.prefix?capFirst}${entity}MpRepository;
import ${cfg.package}.${cfg.moduleName}.${cfg.prefix}.service.${cfg.prefix?capFirst}${entity}Service;

import javax.annotation.Resource;

/**
 * <p>
 * <#if table.comment??>${table.comment!} </#if>服务
 * </p>
 *
 * @author ${author}
 */
@Service
public class ${table.serviceName} {

    @Resource
    private ${cfg.prefix?capFirst}${entity}Service ${cfg.prefix}${entity}Service;

    @Resource
    private ${cfg.prefix?capFirst}${entity}MpRepository ${cfg.prefix}MpRepository;

    @Resource
    private ${entity}MpRepository mpRepository;

}
