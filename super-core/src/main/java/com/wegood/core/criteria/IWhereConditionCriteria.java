package com.wegood.core.criteria;

import cn.hutool.core.lang.Assert;

import java.util.Collection;
import java.util.List;

/**
 * 基于mysql的条件
 *
 * @author Rain
 */
public interface IWhereConditionCriteria<E, C extends IWhereConditionCriteria<E, C>> extends ICriteria<E, C> {

    /**
     * whereConditions 相关方法
     */
    List<WhereCondition> getWhereConditions();

    C setWhereConditions(List<WhereCondition> whereConditions);

    /**
     * 添加查询条件
     */
    default void equals(String column, Object value) {
        getWhereConditions().add(new WhereCondition(column, Condition.EQUAL, value));
    }

    default void orEquals(String column, Object value) {
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.EQUAL, value));
    }

    default void notEquals(String column, Object value) {
        getWhereConditions().add(new WhereCondition(column, Condition.NOT_EQUAL, value));
    }

    default void orNotEquals(String column, Object value) {
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.NOT_EQUAL, value));
    }

    default void lessThan(String column, Object value) {
        getWhereConditions().add(new WhereCondition(column, Condition.LESS_THAN, value));
    }

    default void orLessThan(String column, Object value) {
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.LESS_THAN, value));
    }

    default void lessThanEqual(String column, Object value) {
        getWhereConditions().add(new WhereCondition(column, Condition.LESS_THAN_EQUAL, value));
    }

    default void orLessThanEqual(String column, Object value) {
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.LESS_THAN_EQUAL, value));
    }

    default void greaterThan(String column, Object value) {
        getWhereConditions().add(new WhereCondition(column, Condition.GREATER_THAN, value));
    }

    default void orGreaterThan(String column, Object value) {
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.GREATER_THAN, value));
    }

    default void greaterThanEqual(String column, Object value) {
        getWhereConditions().add(new WhereCondition(column, Condition.GREATER_THAN_EQUAL, value));
    }

    default void orGreaterThanEqual(String column, Object value) {
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.GREATER_THAN_EQUAL, value));
    }

    default void like(String column, String value) {
        Assert.notBlank(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.LIKE, value));
    }

    default void orLike(String column, String value) {
        Assert.notBlank(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.LIKE, value));
    }

    default void notLike(String column, String value) {
        Assert.notBlank(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.NOT_LIKE, value));
    }

    default void orNotLike(String column, String value) {
        Assert.notBlank(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.NOT_LIKE, value));
    }

    default void in(String column, Collection<?> values) {
        Assert.notEmpty(values, "values不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.IN, values));
    }

    default void orIn(String column, Collection<?> values) {
        Assert.notEmpty(values, "values不能为空");
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.IN, values));
    }

    default void notIn(String column, Collection<?> values) {
        Assert.notEmpty(values, "values不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.NOT_IN, values));
    }

    default void orNotIn(String column, Collection<?> values) {
        Assert.notEmpty(values, "values不能为空");
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.NOT_IN, values));
    }

    default void startWith(String column, String value) {
        Assert.notBlank(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.START_WITH, value));
    }

    default void orStartWith(String column, String value) {
        Assert.notBlank(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(null, Condition.OR, null));
        getWhereConditions().add(new WhereCondition(column, Condition.START_WITH, value));
    }

    default C isNull(String column) {
        Assert.notBlank(column, "column不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.IS_NULL, null));
        return (C) this;
    }

    default C isNotNull(String column) {
        Assert.notBlank(column, "column不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.IS_NOT_NULL, null));
        return (C) this;
    }

    default void collectionContainsAll(String column, Collection<String> value) {
        Assert.notEmpty(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.CONTAINS_ALL, value));
    }

    default void collectionContainsAny(String column, Collection<String> value) {
        Assert.notEmpty(value, "value不能为空");
        getWhereConditions().add(new WhereCondition(column, Condition.CONTAINS_ANY, value));
    }

}
