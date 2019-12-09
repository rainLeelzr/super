package ${package.Service?replace(".service",".${cfg.prefix}.service")};

import ${cfg.package}.core.web.IService;
import ${cfg.package}.${cfg.moduleName}.api.criteria.${entity}Criteria;
import ${package.Entity}.${entity};
import ${cfg.package}.${cfg.moduleName}.${cfg.prefix}.repository.${cfg.prefix?cap_first}${entity}MpRepository;
import lombok.Getter;
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
public class ${cfg.prefix?cap_first}${table.serviceName} implements IService<${entity}, ${entity}Criteria> {

    @Getter
    @Resource
    private ${cfg.prefix?cap_first}${entity}MpRepository mpRepository;

}
