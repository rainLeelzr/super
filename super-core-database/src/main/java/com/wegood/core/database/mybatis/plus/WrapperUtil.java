package com.wegood.core.database.mybatis.plus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wegood.core.criteria.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rain
 */
public class WrapperUtil {

    /**
     * 返回 queryWrapper
     */
    public static <E, C extends ICriteria<E, C>> QueryWrapper<E> getQueryWrapper(ICriteria<E, C> criteria) {
        QueryWrapper<E> wrapper = new QueryWrapper<>();

        if (criteria instanceof ISelectColumnCriteria) {
            processSelectColumnsCriteria(wrapper, (ISelectColumnCriteria) criteria);
        }

        if (criteria instanceof IWhereConditionCriteria) {
            processWhereConditionCriteria(wrapper, (IWhereConditionCriteria) criteria);
        }

        if (criteria instanceof IPageCriteria) {
            processPageCriteria(wrapper, (IPageCriteria) criteria);
        }

        return wrapper;
    }

    public static <E, C extends ICriteria<E, C>> UpdateWrapper<E> getUpdateWrapper(ICriteria<E, C> criteria) {
        UpdateWrapper<E> wrapper = new UpdateWrapper<>();

        if (criteria instanceof IWhereConditionCriteria) {
            processWhereConditionCriteria(wrapper, (IWhereConditionCriteria) criteria);
        }

        return wrapper;
    }

    private static void processSelectColumnsCriteria(QueryWrapper wrapper, ISelectColumnCriteria selectColumnCriteria) {
        List<String> selectColumns = selectColumnCriteria.getSelectColumns();
        if (CollUtil.isNotEmpty(selectColumns)) {
            wrapper.select(CollUtil.join(selectColumns, ", "));
        }
    }

    private static void processWhereConditionCriteria(AbstractWrapper wrapper, IWhereConditionCriteria whereConditionCriteria) {
        List<WhereCondition> whereConditions = whereConditionCriteria.getWhereConditions();
        if (CollUtil.isNotEmpty(whereConditions)) {
            whereConditions.forEach(wc -> MybatisPlusWhereCondition.apply(wc, wrapper));
        }
    }

    private static void processPageCriteria(AbstractWrapper wrapper, IPageCriteria pageCriteria) {
        if (StrUtil.isNotBlank(pageCriteria.getOrderBy())) {
            List<String> ascList = null;
            List<String> descList = null;

            String[] split = pageCriteria.getOrderBy().split(StrUtil.COMMA);
            for (String s : split) {
                if (StrUtil.isBlank(s)) {
                    continue;
                }

                s = s.trim().replaceAll(" +", StrUtil.SPACE);
                String[] orderByArr = parseOrderBy(s);
                if (orderByArr == null) {
                    continue;
                }

                if (orderByArr.length == 1) {
                    if (ascList == null) {
                        ascList = new ArrayList<>(split.length);
                    }
                    ascList.add(orderByArr[0]);
                } else if (orderByArr.length == 2) {
                    if (IPageCriteria.ASC.equalsIgnoreCase(orderByArr[1])) {
                        if (ascList == null) {
                            ascList = new ArrayList<>(split.length);
                        }
                        ascList.add(orderByArr[0]);
                    } else if (IPageCriteria.DESC.equalsIgnoreCase(orderByArr[1])) {
                        if (descList == null) {
                            descList = new ArrayList<>(split.length);
                        }
                        descList.add(orderByArr[0]);
                    }
                }
            }
            wrapper.orderByAsc(ascList == null ? null : ArrayUtil.toArray(ascList, String.class));
            wrapper.orderByDesc(descList == null ? null : ArrayUtil.toArray(descList, String.class));
        }
    }

    private static String[] parseOrderBy(String orderBy) {
        String[] strings = orderBy.split(StrUtil.SPACE);
        if (strings.length == 0) {
            return null;
        }

        if (strings.length == 1) {
            return new String[]{strings[0]};
        }

        return new String[]{strings[0], strings[1]};
    }

}
