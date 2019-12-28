<#include "./segment/copyright.ftl">

package ${cfg.package}.${cfg.moduleName}.db.repository;

import org.springframework.stereotype.Repository;
import ${cfg.package}.api.model.${cfg.moduleName}.criteria.${entity}Criteria;
import ${cfg.entityPackageName}.${entity};
import ${cfg.entityDbPackageName}.${entity}Db;
import ${cfg.package}.${cfg.moduleName}.db.mapper.${entity}Mapper;
import ${cfg.package}.core.database.mybatis.plus.MybatisPlusRepository;

/**
 * <p>
 * <#if table.comment??>${table.comment!} </#if>数据仓库
 * </p>
 *
 * @author ${author}
 */
@Repository
public class ${entity}MpRepository extends MybatisPlusRepository<${entity}, ${entity}Db, ${entity}Criteria, ${entity}Mapper> {

}
