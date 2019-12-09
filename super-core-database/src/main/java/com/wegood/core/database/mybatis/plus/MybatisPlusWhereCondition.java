package com.wegood.core.database.mybatis.plus;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.wegood.core.criteria.WhereCondition;

import java.util.Collection;

/**
 * @author Rain
 */
public class MybatisPlusWhereCondition {

    @SuppressWarnings("unchecked")
    public static void apply(WhereCondition whereCondition, AbstractWrapper wrapper) {
        switch (whereCondition.getCondition()) {
            case OR:
                wrapper.or();
                break;
            case EQUAL:
                wrapper.eq(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case NOT_EQUAL:
                wrapper.ne(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case IN:

                if (whereCondition.getValue() == null) {
                    break;
                }
                if (whereCondition.getValue() instanceof Collection) {
                    wrapper.in(whereCondition.getColumnName(), ((Collection) whereCondition.getValue()).toArray());
                } else {
                    wrapper.in(whereCondition.getColumnName(), whereCondition.getValue());
                }
                break;
            case NOT_IN:
                if (whereCondition.getValue() == null) {
                    break;
                }
                if (whereCondition.getValue() instanceof Collection) {
                    wrapper.notIn(whereCondition.getColumnName(), ((Collection) whereCondition.getValue()).toArray());
                } else {
                    wrapper.notIn(whereCondition.getColumnName(), whereCondition.getValue());
                }
                break;
            case IS_NULL:
                if (StrUtil.isNotBlank(whereCondition.getColumnName())) {
                    wrapper.isNull(whereCondition.getColumnName());
                }
                break;
            case IS_NOT_NULL:
                if (StrUtil.isNotBlank(whereCondition.getColumnName())) {
                    wrapper.isNotNull(whereCondition.getColumnName());
                }
                break;
            case GREATER_THAN:
                wrapper.gt(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case GREATER_THAN_EQUAL:
                wrapper.ge(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case LESS_THAN:
                wrapper.lt(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case LESS_THAN_EQUAL:
                wrapper.le(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case START_WITH:
                wrapper.likeRight(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case LIKE:
                wrapper.like(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case NOT_LIKE:
                wrapper.notLike(whereCondition.getValue() != null, whereCondition.getColumnName(), whereCondition.getValue());
                break;
            case CONTAINS_ALL:
                wrapper.apply(
                    whereCondition.getValue() != null,
                    StrUtil.format("{} @> '{{}}'",
                        whereCondition.getColumnName(),
                        CollUtil.join((Collection) whereCondition.getValue(), ",")));
                break;
            case CONTAINS_ANY:
                wrapper.apply(
                    whereCondition.getValue() != null,
                    StrUtil.format("{} && '{{}}'",
                        whereCondition.getColumnName(),
                        CollUtil.join((Collection) whereCondition.getValue(), ",")));
                break;
            default:
                throw new UnsupportedOperationException(StrUtil.format("不支持的[{}]条件转换成mybatis plus wrapper", whereCondition.getCondition()));
        }
    }
}