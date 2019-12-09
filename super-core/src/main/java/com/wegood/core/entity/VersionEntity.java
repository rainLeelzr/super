package com.wegood.core.entity;

/**
 * @author Rain
 */
public interface VersionEntity extends IEntity {

    /**
     * 数据库字段名
     */
    String VERSION = "version";

    /**
     * 默认版本号
     */
    int DEFAULT_VERSION = 1;

    /**
     * 获取版本号
     */
    Integer getVersion();

    /**
     * 设置版本号
     */
    VersionEntity setVersion(Integer id);

    /**
     * 如果版本号为 null, 则设置版本号为1，并返回版本号
     */
    default VersionEntity computeVersionAsOneIfAbsent() {
        if (getVersion() == null) {
            setVersion(1);
        }
        return this;
    }

    @Override
    default VersionEntity randomEntity() {
        return computeVersionAsOneIfAbsent();
    }

}
