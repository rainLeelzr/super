package vip.isass.core.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.isass.core.criteria.ICriteria;
import vip.isass.core.exception.AbsentException;
import vip.isass.core.repository.IRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author rain
 */
public interface IService<E, C extends ICriteria<E, C>> {

    IRepository<E, C> getMpRepository();

    // ****************************** 增 start ******************************
    default E add(E entity) {
        getMpRepository().add(entity);
        return entity;
    }

    default Collection<E> addBatch(Collection<E> entities) {
        getMpRepository().addBatch(entities);
        return entities;
    }

    default Collection<E> addBatch(Collection<E> entities, int batchSize) {
        getMpRepository().addBatch(entities, batchSize);
        return entities;
    }

    default E addIfAbsent(E entity, ICriteria<E, C> criteria) {
        if (this.isAbsentByCriteria(criteria)) {
            return this.add(entity);
        }
        return null;
    }

    // ****************************** 删 start ******************************

    default Boolean deleteById(Serializable id) {
        return getMpRepository().deleteById(id);
    }

    default Boolean deleteByIds(Collection<? extends Serializable> ids) {
        return getMpRepository().deleteByIds(ids);
    }

    default Boolean deleteByCriteria(ICriteria<E, C> criteria) {
        return getMpRepository().deleteByCriteria(criteria);
    }

    //****************************** 改 start ******************************
    default Boolean updateById(E entity) {
        return getMpRepository().updateById(entity);
    }

    default void updateByIdOrException(E entity) {
        if (!getMpRepository().updateById(entity)) {
            throw new AbsentException("更新失败，记录不存在");
        }
    }

    default Boolean updateByCriteria(E entity, ICriteria<E, C> criteria) {
        return getMpRepository().updateByCriteria(entity, criteria);
    }

    // ****************************** 查 start ******************************
    default E getById(Serializable id) {
        return getMpRepository().getById(id);
    }

    default E getByIdOrException(Serializable id) {
        return getMpRepository().getByIdOrException(id);
    }

    default E getByCriteria(ICriteria<E, C> criteria) {
        return getMpRepository().getByCriteria(criteria);
    }

    default E getByCriteriaOrWarn(ICriteria<E, C> criteria) {
        return getMpRepository().getByCriteriaOrWarn(criteria);
    }

    default E getByCriteriaOrException(ICriteria<E, C> criteria) {
        return getMpRepository().getByCriteriaOrException(criteria);
    }

    default List<E> findByCriteria(ICriteria<E, C> criteria) {
        return getMpRepository().findByCriteria(criteria);
    }

    default IPage<E> findPageByCriteria(ICriteria<E, C> criteria) {
        return getMpRepository().findPageByCriteria(criteria);
    }

    default List<E> findAll() {
        return getMpRepository().findAll();
    }

    default Integer countByCriteria(ICriteria<E, C> criteria) {
        return getMpRepository().countByCriteria(criteria);
    }

    default Integer countAll() {
        return getMpRepository().countAll();
    }

    default boolean isPresentById(Serializable id) {
        return getMpRepository().isPresentById(id);
    }

    default boolean isPresentByColumn(String columnName, Object value) {
        return getMpRepository().isPresentByColumn(columnName, value);
    }

    default boolean isPresentByCriteria(ICriteria<E, C> criteria) {
        return getMpRepository().isPresentByCriteria(criteria);
    }

    default boolean isAbsentByCriteria(ICriteria<E, C> criteria) {
        return !isPresentByCriteria(criteria);
    }

    default void exceptionIfPresentByCriteria(ICriteria<E, C> criteria) {
        getMpRepository().exceptionIfPresentByCriteria(criteria);
    }

    default void exceptionIfAbsentByCriteria(ICriteria<E, C> criteria) {
        getMpRepository().exceptionIfAbsentByCriteria(criteria);
    }

}
