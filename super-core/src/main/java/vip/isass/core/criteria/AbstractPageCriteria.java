package vip.isass.core.criteria;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 基于mysql的条件
 *
 * @author Rain
 */
@ToString
@Accessors(chain = true)
public abstract class AbstractPageCriteria<E, C extends AbstractPageCriteria<E, C>> implements IPageCriteria<E, C> {

    /**
     * 分页页码
     */
    private Long pageNum;

    /**
     * 每页大小
     */
    private Long pageSize;

    /**
     * 排序
     * id asc 或者 id desc, modify_Time desc
     */
    @Getter
    private String orderBy;

    private Boolean searchCountFlag;

    public Long getPageNum() {
        return pageNum == null ? DEFAULT_PAGE_NUM : pageNum < 1L ? 1L : pageNum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C setPageNum(Long pageNum) {
        this.pageNum = pageNum;
        return (C) this;
    }

    public Long getPageSize() {
        return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize < 1L ? DEFAULT_PAGE_SIZE : pageSize;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return (C) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return (C) this;
    }

    public Boolean getSearchCountFlag() {
        return searchCountFlag == null ? DEFAULT_SEARCH_COUNT_FLAG : searchCountFlag;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C setSearchCountFlag(Boolean searchCountFlag) {
        this.searchCountFlag = searchCountFlag;
        return (C) this;
    }

}

