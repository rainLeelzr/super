package com.wegood.core.repository;

import com.wegood.core.criteria.ICriteria;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Rain
 */
public interface IRepository<E, C extends ICriteria<E, C>> {

    // ****************************** 增 start ******************************
    default boolean add(E entity) {
        throw new UnsupportedOperationException();
    }

    default boolean addBatch(Collection<E> entities) {
        throw new UnsupportedOperationException();
    }

    default boolean addBatch(Collection<E> entities, int batchSize) {
        throw new UnsupportedOperationException();
    }

    // ****************************** 删 start ******************************

    default boolean deleteById(Serializable id) {
        throw new UnsupportedOperationException();
    }

    default boolean deleteByIds(Collection<? extends Serializable> ids) {
        throw new UnsupportedOperationException();
    }

    default boolean deleteByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    //****************************** 改 start ******************************
    default boolean updateById(E entity) {
        throw new UnsupportedOperationException();
    }

    default boolean updateByCriteria(E entity, ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    // ****************************** 查 start ******************************
    default E getById(Serializable id) {
        throw new UnsupportedOperationException();
    }

    default E getByIdOrException(Serializable id) {
        throw new UnsupportedOperationException();
    }

    default E getByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default E getByCriteriaOrWarn(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default E getByCriteriaOrException(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default List<E> findByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default IPage<E> findPageByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default List<E> findAll() {
        throw new UnsupportedOperationException();
    }

    default Integer countByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default Integer countAll() {
        throw new UnsupportedOperationException();
    }

    default boolean isPresentById(Serializable id) {
        throw new UnsupportedOperationException();
    }

    default boolean isPresentByColumn(String columnName, Object value) {
        throw new UnsupportedOperationException();
    }

    default boolean isPresentByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default void exceptionIfPresentByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

    default void exceptionIfAbsentByCriteria(ICriteria<E, C> criteria) {
        throw new UnsupportedOperationException();
    }

}
