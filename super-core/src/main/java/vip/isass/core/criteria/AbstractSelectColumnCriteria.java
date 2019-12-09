package vip.isass.core.criteria;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Rain
 */
@Getter
@ToString
@Accessors(chain = true)
public abstract class AbstractSelectColumnCriteria<E, C extends AbstractSelectColumnCriteria<E, C>>
    implements ISelectColumnCriteria<E, C> {

    private List<String> selectColumns;

    @Override
    @SuppressWarnings("unchecked")
    public C setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
        return (C) this;
    }

}
