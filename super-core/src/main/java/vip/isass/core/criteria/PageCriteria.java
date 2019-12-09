package vip.isass.core.criteria;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 基于mysql的条件
 *
 * @author Rain
 */
@Setter
@ToString
@Accessors(chain = true)
public class PageCriteria implements IPageCriteria<Object, PageCriteria> {

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

    public Long getPageNum() {
        return pageNum == null ? DEFAULT_PAGE_NUM : pageNum < 1L ? 1L : pageNum;
    }

    public Long getPageSize() {
        return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize < 1L ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public Boolean getSearchCountFlag() {
        return searchCountFlag == null ? DEFAULT_SEARCH_COUNT_FLAG : searchCountFlag;
    }

}

