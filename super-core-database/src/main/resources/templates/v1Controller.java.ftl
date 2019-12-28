<#include "./segment/copyright.ftl">

package ${package.Controller?replace(".controller",".${cfg.prefix}.controller")};

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ${cfg.package}.api.model.${cfg.moduleName}.criteria.${entity}Criteria;
import ${cfg.entityPackageName}.${entity};
import ${package.Service?replace(".service",".${cfg.prefix}.service")}.${cfg.prefix?cap_first}${table.serviceName};
import ${cfg.package}.core.web.IController;
import ${cfg.package}.core.web.Resp;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <#if table.comment??>${table.comment!} </#if>控制器
 * </p>
 *
 * @author ${author}
 */
@Slf4j
@RestController
public class ${cfg.prefix?cap_first}${table.controllerName} implements IController<${entity}> {

    @Resource
    private ${cfg.prefix?cap_first}${table.serviceName} ${cfg.prefix}${table.serviceName};

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/{id}")
    public Resp<${entity}> getById(@PathVariable("id") Serializable id) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.getById(id));
    }

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/1")
    public Resp<${entity}> getByCriteria(@ModelAttribute ${entity}Criteria criteria) {
        return Resp.bizSuccess(v1${entity}Service.getByCriteria(criteria));
    }

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/page")
    public Resp<IPage<${entity}>> findPageByCriteria(@ModelAttribute ${entity}Criteria criteria) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.findPageByCriteria(criteria));
    }

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}")
    public Resp<List<${entity}>> findByCriteria(@ModelAttribute ${entity}Criteria criteria) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.findByCriteria(criteria));
    }

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/count")
    public Resp<Integer> countByCriteria(@ModelAttribute ${entity}Criteria criteria) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.countByCriteria(criteria));
    }

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/count/all")
    public Resp<Integer> countAll() {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.countAll());
    }

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/present/{id}")
    public Resp<Boolean> isPresentById(@PathVariable("id") String id) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.isPresentById(id));
    }

    @GetMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/present")
    public Resp<Boolean> isPresentByCriteria(@ModelAttribute ${entity}Criteria criteria) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.isPresentByCriteria(criteria));
    }

    @PostMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}")
    public Resp<String> add(@RequestBody @Valid ${entity} entity) {
        ${cfg.prefix}${table.serviceName}.add(entity);
        return Resp.bizSuccess(entity.getId());
    }

    @PostMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/batch")
    public Resp<Integer> batchAdd(@RequestBody ArrayList<${entity}> entitys) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.addBatch(entitys).size());
    }

    @PutMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/allColumns")
    public Resp<Boolean> updateAllColumnsById(@RequestBody @Valid ${entity} entity) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.updateEntityById(entity));
    }

    @PutMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}")
    public Resp<Boolean> updateExcludeNullFieldsById(@RequestBody @Valid ${entity} entity) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.updateEntityById(entity));
    }

    @DeleteMapping("/v1/${table.name?replace("${cfg.tablePrefix[0]}_","","f")?replace("_","-")}/{ids}")
    public Resp<Boolean> deleteByIds(@PathVariable("ids") List<String> ids) {
        return Resp.bizSuccess(${cfg.prefix}${table.serviceName}.deleteByIds(ids));
    }

}
