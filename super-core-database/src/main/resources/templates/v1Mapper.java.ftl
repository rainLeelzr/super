package ${package.Mapper?replace(".mapper",".${cfg.prefix}.mapper")};

import ${cfg.package}.core.database.mapper.IMapper;
import ${package.Entity}.${entity};

/**
 * <p>
 * ${table.comment!} Mapper
 * </p>
 *
 * @author ${author}
 */
public interface ${cfg.prefix?cap_first}${table.mapperName} extends IMapper<${entity}> {

}
