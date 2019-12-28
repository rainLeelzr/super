<#include "./segment/copyright.ftl">

package ${package.Mapper};

import ${cfg.entityDbPackageName}.${entity}Db;
import ${cfg.package}.core.database.mapper.IMapper;

/**
 * <p>
 * <#if table.comment??>${table.comment!} </#if>Mapper
 * </p>
 *
 * @author ${author}
 */
public interface ${table.mapperName} extends IMapper<${entity}Db> {

}
