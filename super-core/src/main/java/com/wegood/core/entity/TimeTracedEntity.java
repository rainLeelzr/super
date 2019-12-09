package com.wegood.core.entity;

import com.wegood.core.support.LocalDateTimeUtil;

import java.time.LocalDateTime;

/**
 * @author Rain
 */
public interface TimeTracedEntity extends IEntity {

    /**
     * 数据库字段名
     */
    String CREATED_TIME = "create_time";

    /**
     * java 成员变量名
     */
    String CREATED_TIME_PROPERTY = "createTime";

    String MODIFY_TIME = "modify_time";

    String MODIFY_TIME_PROPERTY = "modifyTime";

    /**
     * 获取创建记录的时间
     */
    LocalDateTime getCreateTime();

    /**
     * 设置创建记录的时间
     */
    TimeTracedEntity setCreateTime(LocalDateTime createTime);

    /**
     * 获取修改记录的时间
     */
    LocalDateTime getModifyTime();

    /**
     * 设置修改记录的时间
     */
    TimeTracedEntity setModifyTime(LocalDateTime modifyTime);

    @Override
    default TimeTracedEntity randomEntity() {
        return setCreateTime(LocalDateTimeUtil.now()).setModifyTime(LocalDateTimeUtil.now());
    }

}
