package vip.isass.core.database.mybatis.plus.driver;

import vip.isass.core.database.mybatis.plus.handler.MybatisPlusParameterHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

/**
 * @author hone 2018/4/28
 * @since
 */
public class MybatisXMLLanguageDriver extends XMLLanguageDriver {

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject,
                                                   BoundSql boundSql) {
        /* 使用自定义 ParameterHandler */
        return new MybatisPlusParameterHandler(mappedStatement, parameterObject, boundSql);
    }
}
