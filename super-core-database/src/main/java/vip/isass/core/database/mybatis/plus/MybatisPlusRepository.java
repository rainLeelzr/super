package vip.isass.core.database.mybatis.plus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import vip.isass.core.criteria.ICriteria;
import vip.isass.core.criteria.IPageCriteria;
import vip.isass.core.database.mapper.IMapper;
import vip.isass.core.entity.IdEntity;
import vip.isass.core.entity.SensitiveDataProperty;
import vip.isass.core.exception.AbsentException;
import vip.isass.core.exception.AlreadyPresentException;
import vip.isass.core.exception.code.StatusMessageEnum;
import vip.isass.core.repository.IRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Rain
 */
@Slf4j
public abstract class MybatisPlusRepository<M extends IMapper<E>, E, C extends ICriteria<E, C>>
    extends ServiceImpl<M, E>
    implements IRepository<E, C> {

    // ****************************** 增 start ******************************
    @Override
    public boolean add(E entity) {
        return super.save(entity);
    }

    @Override
    public boolean addBatch(Collection<E> entities) {
        if (CollUtil.isEmpty(entities)) {
            return false;
        }
        return super.saveBatch(entities);
    }

    @Override
    public boolean addBatch(Collection<E> entities, int batchSize) {
        return super.saveBatch(entities, batchSize);
    }

    // ****************************** 删 start ******************************

    @Override
    public boolean deleteById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean deleteByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    public boolean deleteByWrapper(Wrapper<E> wrapper) {
        return super.remove(wrapper);
    }

    @Override
    public boolean deleteByCriteria(ICriteria<E, C> criteria) {
        return this.deleteByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

    //****************************** 改 start ******************************

    @Override
    public boolean updateById(E entity) {
        Serializable id = ((IdEntity) entity).getId();
        Assert.notNull(id, "id 不能为null");
        if (id instanceof String) {
            Assert.notBlank((String) id, "id 不能为空");
        }
        return super.updateById(entity);
    }

    public boolean updateByWrapper(E entity, Wrapper<E> wrapper) {
        return this.update(entity, wrapper);
    }

    @Override
    public boolean updateByCriteria(E entity, ICriteria<E, C> criteria) {
        return this.updateByWrapper(entity, WrapperUtil.getUpdateWrapper(criteria));
    }

    // ****************************** 查 start ******************************
    @Override
    public E getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public E getByIdOrException(Serializable id) {
        E t = this.getById(id);
        if (t == null) {
            throw new AbsentException(id.toString());
        }
        return t;
    }

    public E getByWrapper(Wrapper<E> wrapper) {
        IPage<E> page = findPageByWrapper(new Page<E>(1, 1).setSearchCount(false), wrapper);
        return page.getRecords().isEmpty() ? null : page.getRecords().get(0);
    }

    @Override
    public E getByCriteria(ICriteria<E, C> criteria) {
        return getByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

    public E getOrWarnByWrapper(Wrapper<E> wrapper) {
        E t = getByWrapper(wrapper);
        if (t == null) {
            log.warn(StatusMessageEnum.ABSENT.getMsg() + ": " + currentModelClass().getSimpleName() + ": " + wrapper.getSqlSegment());
        }
        return t;
    }

    @Override
    public E getByCriteriaOrWarn(ICriteria<E, C> criteria) {
        return getOrWarnByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

    public E getByWrapperOrException(Wrapper<E> wrapper) {
        E entity = getByWrapper(wrapper);
        if (entity == null) {
            throw new AbsentException(wrapper.getSqlSegment());
        }
        return entity;
    }

    @Override
    public E getByCriteriaOrException(ICriteria<E, C> criteria) {
        return getByWrapperOrException(WrapperUtil.getQueryWrapper(criteria));
    }

    public List<E> findByWrapper(Wrapper<E> wrapper) {
        if (wrapper != null && !Optional.ofNullable(wrapper.getSqlSelect()).isPresent() && wrapper instanceof QueryWrapper) {
            ((QueryWrapper<E>) wrapper).select(currentModelClass(), i -> !SensitiveDataProperty.PROPERTIES.contains(i.getProperty()));
        }
        return this.list(wrapper);
    }

    @Override
    public List<E> findByCriteria(ICriteria<E, C> criteria) {
        return this.findByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

    public IPage<E> findPageByWrapper(long pageNum, long pageSize, boolean searchCountFlag, Wrapper<E> wrapper) {
        return this.findPageByWrapper(new Page<>(pageNum, pageSize, searchCountFlag), wrapper);
    }

    public IPage<E> findPageByWrapper(IPage<E> page, Wrapper<E> wrapper) {
        if (wrapper != null && !Optional.ofNullable(wrapper.getSqlSelect()).isPresent() && wrapper instanceof QueryWrapper) {
            ((QueryWrapper<E>) wrapper).select(currentModelClass(), i -> !SensitiveDataProperty.PROPERTIES.contains(i.getProperty()));
        }
        return this.page(page, wrapper);
    }

    @Override
    public IPage<E> findPageByCriteria(ICriteria<E, C> criteria) {
        IPageCriteria pageCriteria = (IPageCriteria) criteria;
        return findPageByWrapper(pageCriteria.getPageNum(), pageCriteria.getPageSize(), pageCriteria.getSearchCountFlag(), WrapperUtil.getQueryWrapper(criteria));
    }

    @Override
    public List<E> findAll() {
        return this.findByWrapper(null);
    }

    public Integer countByWrapper(Wrapper<E> wrapper) {
        return this.count(wrapper);
    }

    @Override
    public Integer countByCriteria(ICriteria<E, C> criteria) {
        return this.countByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

    @Override
    public Integer countAll() {
        return this.count(null);
    }

    @Override
    public boolean isPresentById(Serializable id) {
        Assert.notNull(id, "id");
        if (id instanceof String) {
            Assert.notBlank((String) id, "id");
        }

        return isPresentByWrapper(Wrappers.<E>query().eq(IdEntity.ID_COLUMN_NAME, id));
    }

    @Override
    public boolean isPresentByColumn(String columnName, Object value) {
        Assert.notBlank(columnName);
        Assert.notNull(value, "value");
        return isPresentByWrapper(Wrappers.<E>query().eq(columnName, value));
    }

    public boolean isPresentByWrapper(Wrapper<E> wrapper) {
        return this.countByWrapper(wrapper) > 0;
    }

    @Override
    public boolean isPresentByCriteria(ICriteria<E, C> criteria) {
        return this.isPresentByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

    public void exceptionIfPresentByWrapper(Wrapper<E> wrapper) {
        if (isPresentByWrapper(wrapper)) {
            throw new AlreadyPresentException();
        }
    }

    @Override
    public void exceptionIfPresentByCriteria(ICriteria<E, C> criteria) {
        exceptionIfPresentByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

    public void exceptionIfAbsentByWrapper(Wrapper<E> wrapper) {
        if (!isPresentByWrapper(wrapper)) {
            throw new AbsentException();
        }
    }

    @Override
    public void exceptionIfAbsentByCriteria(ICriteria<E, C> criteria) {
        exceptionIfAbsentByWrapper(WrapperUtil.getQueryWrapper(criteria));
    }

}
