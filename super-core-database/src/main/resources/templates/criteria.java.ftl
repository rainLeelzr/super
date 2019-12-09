<#include "./segment/EntityType.ftl">
package ${cfg.criteriaPackageName};

<#if isIdEntity>
import ${cfg.package}.core.criteria.IdCriteria;
</#if>
<#list table.fields as field>
<#if field.propertyType == "Json">
import com.wegood.core.entity.Json;
<#break>
</#if>
</#list>
import ${package.Entity}.${entity};
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

<#list table.fields as field>
<#if field.propertyType == "BigDecimal">
import java.math.BigDecimal;
<#break>
</#if>
</#list>
<#list table.fields as field>
<#if field.propertyType == "LocalDate">
import java.time.LocalDate;
<#break>
</#if>
</#list>
<#list table.fields as field>
<#if field.propertyType == "LocalDateTime">
import java.time.LocalDateTime;
<#break>
</#if>
</#list>
<#list table.fields as field>
<#if field.propertyType == "LocalTime">
import java.time.LocalTime;
<#break>
</#if>
</#list>
import java.util.Collection;

/**
 * ${table.comment!} 查询条件
 *
 * @author ${author}
 */
@Getter
public class ${entity}Criteria extends <#if isIdEntity>IdCriteria<${entity}Criteria, ${entity}, ${idEntityPropertyType}></#if> {

<#---------- BEGIN 添加成员变量 ------------>
<#list table.fields as field>
<#if field.name?lower_case == "id">
<#continue>
</#if>
    /************************************************** ${field.propertyName} **************************************************/

<#---------- 所有字段类型都有的查询条件 ------------>
<#include "./segment/criteria_property_name_all_type.java.ftl">
<#---------- String字段类型都有的查询条件 ------------>
<#include "./segment/criteria_property_name_string.java.ftl">
<#---------- 数字字段类型都有的查询条件 ------------>
<#include "./segment/criteria_property_name_number.java.ftl">
<#---------- 集合字段类型都有的查询条件 ------------>
<#include "./segment/criteria_property_name_collection.java.ftl">
</#list>
<#---------- END 添加成员变量 ------------>

<#---------- BEGIN 添加setter方法 ------------>
<#list table.fields as field>
<#if field.name == "id">
<#continue>
</#if>
    /************************************************** ${field.propertyName} setter **************************************************/

<#---------- 所有字段类型都有的 setter 方法 ------------>
<#include "./segment/criteria_setter_all_type.java.ftl">
<#---------- String字段类型都有的 setter 方法 ------------>
<#include "./segment/criteria_setter_string.java.ftl">
<#---------- 数字字段类型都有的 setter 方法 ------------>
<#include "./segment/criteria_setter_number.java.ftl">
<#---------- 集合字段类型都有的 setter 方法 ------------>
<#include "./segment/criteria_setter_collection.java.ftl">
</#list>
<#---------- END 添加setter方法 ------------>
}