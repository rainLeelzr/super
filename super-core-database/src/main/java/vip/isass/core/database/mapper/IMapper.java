package vip.isass.core.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author rain
 */
@Mapper
public interface IMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入所有列
     */
    // Integer batchInsertAllColumn(@Param("coll") Collection<T> entityList);

    Integer insertBatchSomeColumn(@Param("list") Collection<T> entityList);

    Integer hardDeleteById(Serializable id);

    Integer hardDeleteByIds(@Param("coll") Collection<Serializable> ids);

}
