package vip.isass.core.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rain
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WhereCondition {

    private String columnName;

    private Condition condition;

    private Object value;

}