package ${cfg.package}.${cfg.moduleName}.repository;

import ${cfg.package}.${cfg.moduleName}.api.criteria.${entity}Criteria;
import org.springframework.stereotype.Repository;
import ${cfg.package}.core.database.mybatis.plus.MybatisPlusRepository;
import ${package.Entity}.${entity};
import ${cfg.package}.${cfg.moduleName}.mapper.${entity}Mapper;

/**
 * <p>
 * ${table.comment!} 数据仓库
 * </p>
 *
 * @author ${author}
 */
@Repository
public class ${entity}MpRepository extends MybatisPlusRepository<${entity}Mapper, ${entity}, ${entity}Criteria> {

}
