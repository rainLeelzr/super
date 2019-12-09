package ${package.Controller};

import ${cfg.package}.core.web.IController;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import ${package.Service?replace(".service",".${cfg.prefix}.service")}.${cfg.prefix?capFirst}${table.serviceName};
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * ${table.comment!} controller
 * </p>
 *
 * @author ${author}
 */
@Slf4j
@RestController
public class ${table.controllerName} implements IController<${entity}> {

    @Resource
    private ${cfg.prefix?capFirst}${entity}Service ${cfg.prefix}${entity}Service;

    @Resource
    private ${table.serviceName} ${table.serviceName?uncapFirst};

}
