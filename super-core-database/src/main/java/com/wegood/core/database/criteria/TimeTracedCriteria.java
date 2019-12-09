package com.wegood.core.database.criteria;

import com.wegood.core.criteria.ICriteria;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Rain
 */
public interface TimeTracedCriteria<E, C extends ICriteria<E, C>> extends ICriteria<E, C> {

    /************************************************** createTime **************************************************/

    C setCreateTime(LocalDateTime createTime);

    C setOrCreateTime(LocalDateTime orCreateTime);

    C setCreateTimeIn(Set<LocalDateTime> createTimeIn);

    C setOrCreateTimeIn(Set<LocalDateTime> orCreateTimeIn);

    C setCreateTimeNotIn(Set<LocalDateTime> createTimeNotIn);

    C setOrCreateTimeNotIn(Set<LocalDateTime> orCreateTimeNotIn);

    C setCreateTimeIn(LocalDateTime... createTimeIn);

    C setOrCreateTimeIn(LocalDateTime... orCreateTimeIn);

    C setCreateTimeNotIn(LocalDateTime... createTimeNotIn);

    C setOrCreateTimeNotIn(LocalDateTime... orCreateTimeNotIn);

    C setCreateTimeNotEqual(LocalDateTime createTimeNotEqual);

    C setOrCreateTimeNotEqual(LocalDateTime orCreateTimeNotEqual);

    C setCreateTimeLessThan(LocalDateTime createTimeLessThan);

    C setOrCreateTimeLessThan(LocalDateTime orCreateTimeLessThan);

    C setCreateTimeLessThanEqual(LocalDateTime createTimeLessThanEqual);

    C setOrCreateTimeLessThanEqual(LocalDateTime orCreateTimeLessThanEqual);

    C setCreateTimeGreaterThan(LocalDateTime createTimeGreaterThan);

    C setOrCreateTimeGreaterThan(LocalDateTime orCreateTimeGreaterThan);

    C setCreateTimeGreaterThanEqual(LocalDateTime createTimeGreaterThanEqual);

    C setOrCreateTimeGreaterThanEqual(LocalDateTime orCreateTimeGreaterThanEqual);

    /************************************************** modifyTime **************************************************/

    C setModifyTime(LocalDateTime modifyTime);

    C setOrModifyTime(LocalDateTime orModifyTime);

    C setModifyTimeIn(Set<LocalDateTime> modifyTimeIn);

    C setOrModifyTimeIn(Set<LocalDateTime> orModifyTimeIn);

    C setModifyTimeNotIn(Set<LocalDateTime> modifyTimeNotIn);

    C setOrModifyTimeNotIn(Set<LocalDateTime> orModifyTimeNotIn);

    C setModifyTimeIn(LocalDateTime... modifyTimeIn);

    C setOrModifyTimeIn(LocalDateTime... orModifyTimeIn);

    C setModifyTimeNotIn(LocalDateTime... modifyTimeNotIn);

    C setOrModifyTimeNotIn(LocalDateTime... orModifyTimeNotIn);

    C setModifyTimeNotEqual(LocalDateTime modifyTimeNotEqual);

    C setOrModifyTimeNotEqual(LocalDateTime orModifyTimeNotEqual);

    C setModifyTimeLessThan(LocalDateTime modifyTimeLessThan);

    C setOrModifyTimeLessThan(LocalDateTime orModifyTimeLessThan);

    C setModifyTimeLessThanEqual(LocalDateTime modifyTimeLessThanEqual);

    C setOrModifyTimeLessThanEqual(LocalDateTime orModifyTimeLessThanEqual);

    C setModifyTimeGreaterThan(LocalDateTime modifyTimeGreaterThan);

    C setOrModifyTimeGreaterThan(LocalDateTime orModifyTimeGreaterThan);

    C setModifyTimeGreaterThanEqual(LocalDateTime modifyTimeGreaterThanEqual);

    C setOrModifyTimeGreaterThanEqual(LocalDateTime orModifyTimeGreaterThanEqual);

    /************************************************** createTime **************************************************/

    LocalDateTime getCreateTime(LocalDateTime createTime);

    LocalDateTime getOrCreateTime(LocalDateTime orCreateTime);

    LocalDateTime getCreateTimeIn(Set<LocalDateTime> createTimeIn);

    LocalDateTime getOrCreateTimeIn(Set<LocalDateTime> orCreateTimeIn);

    LocalDateTime getCreateTimeNotIn(Set<LocalDateTime> createTimeNotIn);

    LocalDateTime getOrCreateTimeNotIn(Set<LocalDateTime> orCreateTimeNotIn);

    LocalDateTime getCreateTimeIn(LocalDateTime... createTimeIn);

    LocalDateTime getOrCreateTimeIn(LocalDateTime... orCreateTimeIn);

    LocalDateTime getCreateTimeNotIn(LocalDateTime... createTimeNotIn);

    LocalDateTime getOrCreateTimeNotIn(LocalDateTime... orCreateTimeNotIn);

    LocalDateTime getCreateTimeNotEqual(LocalDateTime createTimeNotEqual);

    LocalDateTime getOrCreateTimeNotEqual(LocalDateTime orCreateTimeNotEqual);

    LocalDateTime getCreateTimeLessThan(LocalDateTime createTimeLessThan);

    LocalDateTime getOrCreateTimeLessThan(LocalDateTime orCreateTimeLessThan);

    LocalDateTime getCreateTimeLessThanEqual(LocalDateTime createTimeLessThanEqual);

    LocalDateTime getOrCreateTimeLessThanEqual(LocalDateTime orCreateTimeLessThanEqual);

    LocalDateTime getCreateTimeGreaterThan(LocalDateTime createTimeGreaterThan);

    LocalDateTime getOrCreateTimeGreaterThan(LocalDateTime orCreateTimeGreaterThan);

    LocalDateTime getCreateTimeGreaterThanEqual(LocalDateTime createTimeGreaterThanEqual);

    LocalDateTime getOrCreateTimeGreaterThanEqual(LocalDateTime orCreateTimeGreaterThanEqual);

    /************************************************** modifyTime **************************************************/

    LocalDateTime getModifyTime(LocalDateTime modifyTime);

    LocalDateTime getOrModifyTime(LocalDateTime orModifyTime);

    LocalDateTime getModifyTimeIn(Set<LocalDateTime> modifyTimeIn);

    LocalDateTime getOrModifyTimeIn(Set<LocalDateTime> orModifyTimeIn);

    LocalDateTime getModifyTimeNotIn(Set<LocalDateTime> modifyTimeNotIn);

    LocalDateTime getOrModifyTimeNotIn(Set<LocalDateTime> orModifyTimeNotIn);

    LocalDateTime getModifyTimeIn(LocalDateTime... modifyTimeIn);

    LocalDateTime getOrModifyTimeIn(LocalDateTime... orModifyTimeIn);

    LocalDateTime getModifyTimeNotIn(LocalDateTime... modifyTimeNotIn);

    LocalDateTime getOrModifyTimeNotIn(LocalDateTime... orModifyTimeNotIn);

    LocalDateTime getModifyTimeNotEqual(LocalDateTime modifyTimeNotEqual);

    LocalDateTime getOrModifyTimeNotEqual(LocalDateTime orModifyTimeNotEqual);

    LocalDateTime getModifyTimeLessThan(LocalDateTime modifyTimeLessThan);

    LocalDateTime getOrModifyTimeLessThan(LocalDateTime orModifyTimeLessThan);

    LocalDateTime getModifyTimeLessThanEqual(LocalDateTime modifyTimeLessThanEqual);

    LocalDateTime getOrModifyTimeLessThanEqual(LocalDateTime orModifyTimeLessThanEqual);

    LocalDateTime getModifyTimeGreaterThan(LocalDateTime modifyTimeGreaterThan);

    LocalDateTime getOrModifyTimeGreaterThan(LocalDateTime orModifyTimeGreaterThan);

    LocalDateTime getModifyTimeGreaterThanEqual(LocalDateTime modifyTimeGreaterThanEqual);

    LocalDateTime getOrModifyTimeGreaterThanEqual(LocalDateTime orModifyTimeGreaterThanEqual);

}