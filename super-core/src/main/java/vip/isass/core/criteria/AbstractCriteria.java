package vip.isass.core.criteria;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于mysql的条件
 *
 * @author Rain
 */
@ToString
@Accessors(chain = true)
public abstract class AbstractCriteria<E, C extends AbstractCriteria<E, C>>
    implements ISelectColumnCriteria<E, C>, IWhereConditionCriteria<E, C>, IPageCriteria<E, C> {

    // region selectColumn

    @Getter
    private List<String> selectColumns;

    // endregion selectColumn

    // region whereCondition

    private List<WhereCondition> whereConditions;

    // endregion whereCondition

    // region page
    /**
     * 分页页码
     */
    public Long pageNum;

    /**
     * 每页大小
     */
    public Long pageSize;

    /**
     * 排序
     * id asc 或者 id desc, modify_Time desc
     */
    @Getter
    public String orderBy;

    private Boolean searchCountFlag;

    // endregion page

    // region selectColumnCriteria

    @Override
    @SuppressWarnings("unchecked")
    public C setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
        return (C) this;
    }

    // endregion selectColumnCriteria

    // region whereConditionCriteria

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

    // endregion whereConditionCriteria

    // region pageCriteria

    @Override
    public Long getPageNum() {
        return pageNum == null ? DEFAULT_PAGE_NUM : pageNum < 1L ? 1L : pageNum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C setPageNum(Long pageNum) {
        this.pageNum = pageNum;
        return (C) this;
    }

    @Override
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

    @Override
    public Boolean getSearchCountFlag() {
        return searchCountFlag == null ? DEFAULT_SEARCH_COUNT_FLAG : searchCountFlag;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C setSearchCountFlag(Boolean searchCountFlag) {
        this.searchCountFlag = searchCountFlag;
        return (C) this;
    }

    // endregion pageCriteria

}
