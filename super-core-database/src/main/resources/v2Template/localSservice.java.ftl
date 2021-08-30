<#include "./segment/copyright.ftl">

package ${cfg.servicePackageName};

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${cfg.package}.${cfg.moduleName}.api.criteria.${entity}Criteria;
import ${cfg.package}.${cfg.moduleName}.api.entity.${table.serviceName};
import ${cfg.package}.${cfg.moduleName}.api.service.v2.${serviceName};
import ${cfg.package}.${cfg.moduleName}.db.repository.${entity}Repository;
import vip.isass.core.structure.service.IV2LocalService;

/**
 * <p>
 * <#if table.comment??>${table.comment}<#else>${table.name}</#if> 服务
 * </p>
 *
 * @author ${author}
 */
@Service
public class ${table.serviceImplName} implements ${table.serviceName}, IV2LocalService<${entity}, ${entity}Criteria> {

    @Getter
    @Autowired
    private ${table.serviceImplName} service;

    @Getter
    @Autowired
    private ${entity}Repository repository;

}
