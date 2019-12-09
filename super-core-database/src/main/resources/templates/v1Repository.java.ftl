package ${cfg.package}.${cfg.moduleName}.${cfg.prefix}.repository;

import ${cfg.package}.core.database.mybatis.plus.MybatisPlusRepository;
import ${cfg.package}.${cfg.moduleName}.api.criteria.${entity}Criteria;
import ${cfg.package}.${cfg.moduleName}.api.entity.${entity};
import ${cfg.package}.${cfg.moduleName}.${cfg.prefix}.mapper.${cfg.prefix?cap_first}${entity}Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * ${table.comment!} 数据仓库
 * </p>
 *
 * @author ${author}
 */
@Repository
public class ${cfg.prefix?cap_first}${entity}MpRepository extends MybatisPlusRepository<${cfg.prefix?cap_first}${entity}Mapper, ${entity}, ${entity}Criteria> {

}
