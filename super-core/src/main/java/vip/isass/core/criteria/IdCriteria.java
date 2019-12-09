package vip.isass.core.criteria;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import vip.isass.core.entity.IdEntity;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * 基于mysql的条件
 *
 * @author Rain
 */
@Getter
@Accessors(chain = true)
public class IdCriteria<
    C extends IdCriteria<C, E, PK>,
    E extends IdEntity,
    PK extends Serializable>
    extends AbstractCriteria<E, C> {

    /************************************************** id **************************************************/

    private PK id;

    private PK orId;

    private Collection<PK> idIn;

    private Collection<PK> orIdIn;

    private Collection<PK> idNotIn;

    private Collection<PK> orIdNotIn;

    private String idLike;

    private String orIdLike;

    private String idNotLike;

    private String orIdNotLike;

    private String idStartWith;

    private String orIdStartWith;

    /************************************************** id setter **************************************************/

    @SuppressWarnings("unchecked")
    public C setId(PK id) {
        this.id = id;
        equals(IdEntity.ID_COLUMN_NAME, this.id);
        return (C) this;
    }

    public C setOrId(PK id) {
        this.orId = id;
        orEquals(IdEntity.ID_COLUMN_NAME, this.orId);
        return (C) this;
    }

    public C setIdIn(Collection<PK> ids) {
        this.idIn = ids;
        in(IdEntity.ID_COLUMN_NAME, this.idIn);
        return (C) this;
    }

    public C setOrIdIn(Collection<PK> ids) {
        this.orIdIn = ids;
        orIn(IdEntity.ID_COLUMN_NAME, this.orIdIn);
        return (C) this;
    }

    public C setIdNotIn(Collection<PK> ids) {
        this.idNotIn = ids;
        notIn(IdEntity.ID_COLUMN_NAME, this.idNotIn);
        return (C) this;
    }

    public C setOrIdNotIn(Collection<PK> ids) {
        this.orIdNotIn = ids;
        orNotIn(IdEntity.ID_COLUMN_NAME, this.orIdNotIn);
        return (C) this;
    }

    @JsonIgnore
    public C setIdIn(PK... ids) {
        this.idIn = CollUtil.newHashSet(ids);
        in(IdEntity.ID_COLUMN_NAME, this.idIn);
        return (C) this;
    }

    @JsonIgnore
    public C setOrIdIn(PK... ids) {
        this.orIdIn = CollUtil.newHashSet(ids);
        orIn(IdEntity.ID_COLUMN_NAME, this.orIdIn);
        return (C) this;
    }

    @JsonIgnore
    public C setIdNotIn(PK... ids) {
        this.idNotIn = CollUtil.newHashSet(ids);
        notIn(IdEntity.ID_COLUMN_NAME, this.idNotIn);
        return (C) this;
    }

    @JsonIgnore
    public C setOrIdNotIn(PK... ids) {
        this.orIdNotIn = CollUtil.newHashSet(ids);
        orNotIn(IdEntity.ID_COLUMN_NAME, this.orIdNotIn);
        return (C) this;
    }

    public C setIdLike(String idLike) {
        this.idLike = idLike;
        like(IdEntity.ID_COLUMN_NAME, this.idLike);
        return (C) this;
    }

    public C setOrIdLike(String orIdLike) {
        this.orIdLike = orIdLike;
        orLike(IdEntity.ID_COLUMN_NAME, this.orIdLike);
        return (C) this;
    }

    public C setIdNotLike(String idNotLike) {
        this.idNotLike = idNotLike;
        notLike(IdEntity.ID_COLUMN_NAME, this.idNotLike);
        return (C) this;
    }

    public C setOrIdNotLike(String orIdNotLike) {
        this.orIdNotLike = orIdNotLike;
        orNotLike(IdEntity.ID_COLUMN_NAME, this.orIdNotLike);
        return (C) this;
    }

    public C setIdStartWith(String idStartWith) {
        this.idStartWith = idStartWith;
        startWith(IdEntity.ID_COLUMN_NAME, idStartWith);
        return (C) this;
    }

    public C setOrIdStartWith(String orIdStartWith) {
        this.orIdStartWith = orIdStartWith;
        orStartWith(IdEntity.ID_COLUMN_NAME, orIdStartWith);
        return (C) this;
    }


    public C selectId() {
        this.setSelectColumns(IdEntity.ID_COLUMN_NAME);
        return (C) this;
    }

}
