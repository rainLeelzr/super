package vip.isass.core.database.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import vip.isass.core.criteria.ICriteria;
import vip.isass.core.criteria.IdCriteria;
import vip.isass.core.database.criteria.TimeTracedCriteria;
import vip.isass.core.entity.IEntity;
import vip.isass.core.entity.IdEntity;
import vip.isass.core.entity.LogicDeleteEntity;
import vip.isass.core.entity.TimeTracedEntity;
import vip.isass.core.repository.IRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface RepositoryTest<C extends ICriteria<E, C>, E extends IEntity<E>, R extends IRepository<E, C>> {

    C getCriteria();

    E genEntity();

    R getRepository();
    // ****************************** 增 start ******************************

    default void testInsert() {
        E entity = genEntity();
        Assert.isTrue(getRepository().add(entity));
    }

    default void testBatchInsert() {
        int testBatchCount = 10;
        List<E> entitys = new ArrayList<>(testBatchCount);
        for (int i = 0; i < testBatchCount; i++) {
            entitys.add(genEntity());
        }
        Assert.isTrue(getRepository().addBatch(entitys));
    }

    // ****************************** 删 start ******************************

    default void testDeleteById() {
        E entity = genEntity();
        getRepository().add(entity);
        Assert.isTrue(getRepository().deleteById(((IdEntity) entity).getId()));
    }

    default void testDeleteByIds() {
        int testBatchCount = 10;
        List<E> entitys = new ArrayList<>(testBatchCount);
        for (int i = 0; i < testBatchCount; i++) {
            entitys.add(genEntity());
        }
        Assert.isTrue(getRepository().addBatch(entitys));

        Set<Serializable> ids = entitys.stream()
            .filter(e -> e instanceof IdEntity)
            .map(e -> ((IdEntity) e).getId())
            .collect(Collectors.toSet());
        Assert.isTrue(getRepository().deleteByIds(ids));
    }

    //****************************** 改 start ******************************

    default void testUpdateById() {
        E entity = genEntity();
        if (entity instanceof LogicDeleteEntity) {
            ((LogicDeleteEntity) entity).setDeleteFlag(Boolean.FALSE);
        }
        getRepository().add(entity);

        Serializable id = ((IdEntity) entity).getId();
        entity.randomEntity();
        ((IdEntity) entity).setId(id);
        getRepository().updateById(entity);
    }

    default void testUpdateByCriteria() {
        E entity = genEntity();
        if (entity instanceof LogicDeleteEntity) {
            ((LogicDeleteEntity) entity).setDeleteFlag(Boolean.FALSE);
        }
        Assert.isTrue(getRepository().add(entity));

        E newEntity = genEntity();
        if (newEntity instanceof TimeTracedEntity) {
            TimeTracedEntity timeTracedEntity = (TimeTracedEntity) newEntity;
            timeTracedEntity.setCreateTime(null);
        }

        ICriteria<E, C> criteria = getCriteria();
        if (criteria instanceof IdCriteria) {
            IdEntity idEntity = (IdEntity) entity;
            ((IdCriteria) criteria).setId(idEntity.getId());
        }
        if (entity instanceof TimeTracedEntity && criteria instanceof TimeTracedCriteria) {
            TimeTracedEntity timeTracedEntity = (TimeTracedEntity) entity;
            ((TimeTracedCriteria) criteria).setCreateTimeIn(timeTracedEntity.getCreateTime());
        }
        Assert.isTrue(getRepository().updateByCriteria(entity, criteria));

    }

    // ****************************** 查 start ******************************

    default void testGetById() {
        E entity = genEntity();
        if (!(entity instanceof IdEntity)) {
            return;
        }

        if (entity instanceof LogicDeleteEntity) {
            ((LogicDeleteEntity) entity).setDeleteFlag(Boolean.FALSE);
        }
        Assert.isTrue(getRepository().add(entity));

        IdEntity idEntity = (IdEntity) entity;
        IdEntity e = (IdEntity) getRepository().getById(idEntity.getId());

        Assert.notNull(getRepository().getById(e.getId()));
    }

    default void testGetAnyOneByCriteria() {
        E entity = genEntity();
        Assert.isTrue(getRepository().add(entity));

        C criteria = getCriteria();
        if (entity instanceof IdEntity && criteria instanceof IdCriteria) {
            IdEntity idEntity = (IdEntity) entity;
            IdCriteria idCriteria = (IdCriteria) criteria;
            idCriteria.setId(idEntity.getId()).setSelectColumns(CollUtil.newArrayList(idEntity.getIdColumnName()));
        }
        Assert.notNull(getRepository().getByCriteria(criteria));
    }

    default void testFindByCriteria() {
        E entity = genEntity();
        if (entity instanceof LogicDeleteEntity) {
            ((LogicDeleteEntity) entity).setDeleteFlag(Boolean.FALSE);
        }
        Assert.isTrue(getRepository().add(entity));

        C criteria = getCriteria();
        if (entity instanceof IdEntity && criteria instanceof IdCriteria) {
            IdEntity idEntity = (IdEntity) entity;
            IdCriteria idCriteria = (IdCriteria) criteria;
            idCriteria.setId(idEntity.getId()).setSelectColumns(CollUtil.newArrayList(idEntity.getIdColumnName()));
        }
        Assert.isTrue(getRepository().findByCriteria(criteria).size() == 1);
    }


}
