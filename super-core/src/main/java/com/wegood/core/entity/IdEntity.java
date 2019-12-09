package com.wegood.core.entity;

import com.wegood.core.sequence.impl.LongSequence;
import com.wegood.core.sequence.impl.UuidSequence;
import cn.hutool.core.util.StrUtil;

import java.beans.Transient;
import java.io.Serializable;

/**
 * @author Rain
 */
public interface IdEntity<PK extends Serializable, E extends IdEntity> extends IEntity {

    String ID_COLUMN_NAME = "id";

    /**
     * 获取 id
     */
    PK getId();

    /**
     * 设置 id
     */
    E setId(PK id);

    @Transient
    default String getIdColumnName() {
        return ID_COLUMN_NAME;
    }

    /**
     * 生成一个随机id
     */
    E randomId();

    /**
     * 如果 id 为 null, 则生成一个随机 id，并返回 id
     */
    default E computeIdIfAbsent() {
        if (getId() == null) {
            randomId();
        }
        return (E) this;
    }

    @Override
    default E randomEntity() {
        return computeIdIfAbsent();
    }

    static void main(String[] args) {
        System.out.println(Long.MIN_VALUE);
        System.out.println(Long.MAX_VALUE);
        System.out.println(StrUtil.fillBefore(LongSequence.get() + "", '0', 19));
        System.out.println(UuidSequence.get());
    }

}
