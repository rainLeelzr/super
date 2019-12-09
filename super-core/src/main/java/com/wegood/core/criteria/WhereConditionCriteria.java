package com.wegood.core.criteria;

import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于mysql的条件
 *
 * @author Rain
 */
@Accessors(chain = true)
public class WhereConditionCriteria<E, C extends IWhereConditionCriteria<E, C>>
    implements IWhereConditionCriteria<E, C> {

    /**
     * where条件
     */
    private List<WhereCondition> whereConditions;

    @Override
    public List<WhereCondition> getWhereConditions() {
        return whereConditions == null ? whereConditions = new ArrayList<>() : whereConditions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C setWhereConditions(List<WhereCondition> whereConditions) {
        this.whereConditions = whereConditions;
        return (C) this;
    }

}
