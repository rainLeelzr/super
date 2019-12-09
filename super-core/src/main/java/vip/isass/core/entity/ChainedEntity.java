package vip.isass.core.entity;

/**
 * @author Rain
 */
public interface ChainedEntity<PK> extends IEntity {

    String PARENT_ID_COLUMN_NAME = "parent_id";

    String TOP_ID_VALUE = "";

    /**
     * 获取父 id
     */
    PK getParentId();

    /**
     * 设置父 id
     */
    ChainedEntity setParentId(PK parentId);

    /**
     * 标记为顶级实体
     */
    ChainedEntity markAsTopEntity();

    @Override
    default ChainedEntity randomEntity() {
        return markAsTopEntity();
    }

}
