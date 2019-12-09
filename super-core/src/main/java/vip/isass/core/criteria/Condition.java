package vip.isass.core.criteria;


/**
 * @author Rain
 */
public enum Condition {

    // 条件之间的关系
    OR(),

    // 通用类型
    EQUAL(),
    NOT_EQUAL(),

    IN(),
    NOT_IN(),

    IS_NULL(),
    IS_NOT_NULL(),

    // 数字类型
    GREATER_THAN(),
    GREATER_THAN_EQUAL(),
    LESS_THAN(),
    LESS_THAN_EQUAL(),

    // 字符串类型
    START_WITH(),
    LIKE(),
    NOT_LIKE(),

    // 数组类型
    CONTAINS_ALL(),
    CONTAINS_ANY()

}