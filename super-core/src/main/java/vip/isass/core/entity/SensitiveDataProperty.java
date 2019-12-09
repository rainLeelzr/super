package vip.isass.core.entity;

import cn.hutool.core.collection.CollUtil;

import java.util.HashSet;

/**
 * 敏感数据属性名
 * 查数据库时，默认不查询这些字段
 *
 * @author Rain
 */
public interface SensitiveDataProperty {

    HashSet<String> PROPERTIES = CollUtil.newHashSet(
        LogicDeleteEntity.DELETE_FLAG_PROPERTY
        , TimeTracedEntity.CREATED_TIME_PROPERTY
        , TimeTracedEntity.MODIFY_TIME_PROPERTY
        , UserTracedEntity.CREATE_USER_ID_PROPERTY
        , UserTracedEntity.CREATE_USER_NAME_PROPERTY
        , UserTracedEntity.MODIFY_USER_ID_PROPERTY
        , UserTracedEntity.MODIFY_USER_NAME_PROPERTY);

}
