package vip.isass.core.database.postgresql.convert;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import vip.isass.core.database.util.ExtDbColumnType;

/**
 * 代码生成器需要用到的类型转换
 *
 * @author Rain
 */
public class PostgreSqlTypeConvert implements ITypeConvert {

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        // 数字类型
        if ("smallint".equals(t)) {
            return DbColumnType.INTEGER;
        }
        if ("smallint[]".equals(t)) {
            return ExtDbColumnType.INTEGER_ARRAY;
        }
        if ("integer".equals(t)) {
            return DbColumnType.INTEGER;
        }
        if ("integer[]".equals(t)) {
            return ExtDbColumnType.INTEGER_ARRAY;
        }
        if ("bigint".equals(t)) {
            return DbColumnType.LONG;
        }
        if ("bigint[]".equals(t)) {
            return ExtDbColumnType.LONG_ARRAY;
        }
        if (t.startsWith("numeric") && t.endsWith("[]")) {
            return ExtDbColumnType.BIG_DECIMAL_ARRAY;
        }
        if (t.startsWith("numeric")) {
            return DbColumnType.BIG_DECIMAL;
        }

        // 布尔,位
        if ("boolean".equals(t)) {
            return DbColumnType.BOOLEAN;
        }
        if ("boolean[]".equals(t)) {
            return ExtDbColumnType.BOOLEAN_ARRAY;
        }
        if ("bit".equals(t)) {
            return DbColumnType.BOOLEAN;
        }
        if (t.startsWith("bit varying")) {
            return DbColumnType.BYTE_ARRAY;
        }

        // 字符串类型
        if (t.startsWith("character varying") && t.endsWith("[]")) {
            return ExtDbColumnType.STRING_COLLECTION;
        }
        if (t.startsWith("character varying")) {
            return DbColumnType.STRING;
        }
        if (t.startsWith("character") && t.endsWith("[]")) {
            return ExtDbColumnType.STRING_COLLECTION;
        }
        if (t.startsWith("character")) {
            return DbColumnType.STRING;
        }
        if ("text".equals(t)) {
            return DbColumnType.STRING;
        }
        if ("text[]".equals(t)) {
            return ExtDbColumnType.STRING_COLLECTION;
        }

        // 日期时间类型
        if ("date".equals(t)) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return DbColumnType.DATE;
                case SQL_PACK:
                    return DbColumnType.DATE_SQL;
                case TIME_PACK:
                    return DbColumnType.LOCAL_DATE;
                default:
                    return DbColumnType.DATE;
            }
        }
        if ("date[]".equals(t)) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return ExtDbColumnType.DATE_ARRAY;
                case SQL_PACK:
                    return ExtDbColumnType.DATE_SQL_ARRAY;
                case TIME_PACK:
                    return ExtDbColumnType.LOCAL_DATE_ARRAY;
                default:
                    return ExtDbColumnType.DATE_ARRAY;
            }
        }
        if (t.startsWith("timestamp") && t.endsWith("[]")) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return ExtDbColumnType.DATE_ARRAY;
                case SQL_PACK:
                    return ExtDbColumnType.TIMESTAMP_ARRAY;
                case TIME_PACK:
                    return ExtDbColumnType.LOCAL_DATE_TIME_ARRAY;
                default:
                    return ExtDbColumnType.DATE_ARRAY;
            }
        }
        if (t.startsWith("timestamp")) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return DbColumnType.DATE;
                case SQL_PACK:
                    return DbColumnType.TIMESTAMP;
                case TIME_PACK:
                    return DbColumnType.LOCAL_DATE_TIME;
                default:
                    return DbColumnType.DATE;
            }
        }
        if (t.startsWith("time") && t.endsWith("[]")) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return ExtDbColumnType.DATE_ARRAY;
                case SQL_PACK:
                    return ExtDbColumnType.TIME_ARRAY;
                case TIME_PACK:
                    return ExtDbColumnType.LOCAL_TIME_ARRAY;
                default:
                    return ExtDbColumnType.DATE_ARRAY;
            }
        }
        if (t.startsWith("time")) {
            switch (globalConfig.getDateType()) {
                case ONLY_DATE:
                    return DbColumnType.DATE;
                case SQL_PACK:
                    return DbColumnType.TIME;
                case TIME_PACK:
                    return DbColumnType.LOCAL_TIME;
                default:
                    return DbColumnType.DATE;
            }
        }
        if ("json".equals(t)) {
            return DbColumnType.STRING;
        }
        if ("json[]".equals(t)) {
            return ExtDbColumnType.STRING_COLLECTION;
        }
        if ("jsonb".equals(t)) {
            return ExtDbColumnType.JSONB;
        }
        if ("jsonb[]".equals(t)) {
            return ExtDbColumnType.JSONB_ARRAY;
        }

        // 未适配的类型,默认为string
        return DbColumnType.STRING;
    }

}
