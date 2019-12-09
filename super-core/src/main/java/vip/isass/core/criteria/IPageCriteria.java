package vip.isass.core.criteria;

import cn.hutool.core.util.StrUtil;
import vip.isass.core.entity.TimeTracedEntity;
import vip.isass.core.page.PageConst;

/**
 * 基于mysql的条件
 *
 * @author Rain
 */
public interface IPageCriteria<E, C extends IPageCriteria<E, C>> extends ICriteria<E, C> {

    Long DEFAULT_PAGE_NUM = 1L;

    Long DEFAULT_PAGE_SIZE = 20L;

    Boolean DEFAULT_SEARCH_COUNT_FLAG = Boolean.TRUE;

    String ASC = "asc";

    String DESC = "desc";

    Long getPageNum();

    C setPageNum(Long pageNum);

    Long getPageSize();

    C setPageSize(Long pageSize);

    default C setMaxPageSize() {
        return setPageSize(PageConst.MAX_PAGE_SIZE);
    }

    String getOrderBy();

    C setOrderBy(String orderBy);

    @SuppressWarnings("unchecked")
    default C orderByCreateTimeDescIfBlank() {
        if (StrUtil.isBlank(getOrderBy())) {
            orderByCreateTimeDesc();
        }
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    default C orderByModifyTimeDescIfBlank() {
        if (StrUtil.isBlank(getOrderBy())) {
            orderByModifyTimeDesc();
        }
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    default C orderByCreateTimeDesc() {
        setOrderBy(TimeTracedEntity.CREATED_TIME + StrUtil.SPACE + DESC);
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    default C orderByModifyTimeDesc() {
        setOrderBy(TimeTracedEntity.MODIFY_TIME + StrUtil.SPACE + DESC);
        return (C) this;
    }

    Boolean getSearchCountFlag();

    C setSearchCountFlag(Boolean searchCountFlag);

}

