package vip.isass.core.database.mybatis.handler.type;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;


/**
 * 处理字段类型为 BigDecimal[] 的数据库映射关系
 *
 * @author Rain
 */
@Slf4j
@Component
@MappedJdbcTypes(JdbcType.ARRAY)
public class BigDecimalArrayTypeHandler extends BaseTypeHandler<BigDecimal[]> {

    private static final String TYPE_NAME_NUMERIC = "numeric";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal[] parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(TYPE_NAME_NUMERIC, parameter);
        ps.setArray(i, array);
    }

    @Override
    public BigDecimal[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getArray(rs.getArray(columnName));
    }

    @Override
    public BigDecimal[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getArray(rs.getArray(columnIndex));
    }

    @Override
    public BigDecimal[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getArray(cs.getArray(columnIndex));
    }

    private BigDecimal[] getArray(Array array) {
        if (array == null) {
            return null;
        }
        try {
            return (BigDecimal[]) array.getArray();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


}