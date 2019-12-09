package ${package.Service};

import ${cfg.package}.${cfg.moduleName}.repository.${entity}MpRepository;
import ${cfg.package}.${cfg.moduleName}.${cfg.prefix}.repository.${cfg.prefix?capFirst}${entity}MpRepository;
import ${cfg.package}.${cfg.moduleName}.${cfg.prefix}.service.${cfg.prefix?capFirst}${entity}Service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * ${table.comment!} 服务
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
