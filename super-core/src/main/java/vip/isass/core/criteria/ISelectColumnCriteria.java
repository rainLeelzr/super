package vip.isass.core.criteria;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Rain
 */
public interface ISelectColumnCriteria<E, C extends ISelectColumnCriteria<E, C>> extends ICriteria<E, C> {

    String DISTINCT = "DISTINCT ";

    /**
     * selectColumns 相关方法
     */
    List<String> getSelectColumns();

    C setSelectColumns(List<String> selectColumns);

    default C setSelectColumns(String selectColumns) {
        setSelectColumns(Collections.emptyList());
        return addSelectColumns(selectColumns);
    }

    @SuppressWarnings("unchecked")
    default C setSelectColumns(String... selectColumns) {
        setSelectColumns(Collections.emptyList());
        addSelectColumns(selectColumns);
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    default C addSelectColumn(String column) {
        return addSelectColumns(column);
    }

    @SuppressWarnings("unchecked")
    default C addSelectColumns(String... columns) {
        if (ArrayUtil.isEmpty(columns)) {
            return (C) this;
        }

        List<String> selectColumns = getSelectColumns();
        if (CollUtil.isEmpty(selectColumns)) {
            selectColumns = CollUtil.toList(columns);
        } else {
            selectColumns.addAll(CollUtil.toList(columns));
            selectColumns = CollUtil.distinct(selectColumns);
        }
        setSelectColumns(selectColumns);
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    default C addSelectColumns(Collection<String> columns) {
        if (CollUtil.isEmpty(columns)) {
            return (C) this;
        }
        columns.forEach(this::addSelectColumns);
        return (C) this;
    }

}
