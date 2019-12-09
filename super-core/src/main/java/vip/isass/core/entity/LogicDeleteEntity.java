package vip.isass.core.entity;

/**
 * @author Rain
 */
public interface LogicDeleteEntity<T> extends IEntity {

    String DELETE_FLAG = "delete_flag";

    String DELETE_FLAG_PROPERTY = "deleteFlag";

    Boolean DEFAULT_DELETE_FLAG = Boolean.FALSE;

    /**
     * 获取删除标识
     */
    Boolean getDeleteFlag();

    /**
     * 设置删除标识
     */
    T setDeleteFlag(Boolean deleteFlag);

    /**
     * 如果删除标识为 null, 则设置删除标识为 false，并返回删除标识
     */
    default T computeDefaultDeleteFlagIfAbsent() {
        if (getDeleteFlag() == null) {
            setDeleteFlag(Boolean.FALSE);
        }
        return (T) this;
    }

    @Override
    default T randomEntity() {
        setDeleteFlag(randomBoolean());
        return (T) this;
    }

}
