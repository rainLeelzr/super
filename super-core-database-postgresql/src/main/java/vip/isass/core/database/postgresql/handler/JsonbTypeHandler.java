package vip.isass.core.database.postgresql.handler;

import vip.isass.core.database.postgresql.entity.JsonPg;
import vip.isass.core.entity.Json;
import vip.isass.core.support.json.DefaultJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import vip.isass.core.database.postgresql.entity.JsonPg;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 处理字段类型为 Jsonb 的数据库映射关系
 *
 * @author Rain
 */
@Slf4j
@Component
@MappedJdbcTypes(JdbcType.JAVA_OBJECT)
@MappedTypes({Json.class, JsonPg.class, DefaultJson.class})
public class JsonbTypeHandler extends BaseTypeHandler<Json> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Json parameter, JdbcType jdbcType) throws SQLException {
        if (parameter instanceof JsonPg) {
            ps.setObject(i, parameter);
        } else {
            ps.setObject(i, new JsonPg().fromJson(parameter));
        }
    }

    @Override
    public Json getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getJson(rs.getString(columnName));
    }

    @Override
    public Json getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getJson(rs.getString(columnIndex));
    }

    @Override
    public Json getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getJson(cs.getString(columnIndex));
    }

    private Json getJson(String value) {
        if (value == null) {
            return null;
        }
        return new JsonPg().fromString(value);
    }

}