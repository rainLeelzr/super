package com.wegood.core.database.mybatis.handler.type;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.stereotype.Component;

import java.sql.*;


/**
 * 处理字段类型为 Long[] 的数据库映射关系
 *
 * @author Rain
 */
@Slf4j
@Component
@MappedJdbcTypes(JdbcType.ARRAY)
public class LongArrayTypeHandler extends BaseTypeHandler<Long[]> {

    private static final String TYPE_NAME_BIGINT = "bigint";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(TYPE_NAME_BIGINT, parameter);
        ps.setArray(i, array);
    }

    @Override
    public Long[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getArray(rs.getArray(columnName));
    }

    @Override
    public Long[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getArray(rs.getArray(columnIndex));
    }

    @Override
    public Long[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getArray(cs.getArray(columnIndex));
    }

    private Long[] getArray(Array array) {
        if (array == null) {
            return null;
        }
        try {
            return (Long[]) array.getArray();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


}