package com.wegood.core.database.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.wegood.core.database.mybatis.plus.driver.MybatisXMLLanguageDriver;
import com.wegood.core.database.mybatis.plus.handler.MybatisPlusMetaObjectHandler;
import com.wegood.core.page.PageConst;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author rain
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.wegood.**.mapper")
public class SqlSessionConfig implements TransactionManagementConfigurer {

    @Resource
    private DataSource dataSource;

    @Value("${spring.profiles:default}")
    private String profiles;

    @Autowired(required = false)
    private List<BaseTypeHandler> baseTypeHandlers;

    // @Bean
    // public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    //     return new SqlSessionTemplate(sqlSessionFactory);
    // }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setLogicDeleteValue(Boolean.TRUE.toString());
        dbConfig.setLogicNotDeleteValue(Boolean.FALSE.toString());
        GlobalConfig conf = new GlobalConfig();
        conf.setBanner(false);
        // conf.setSqlInjector(new BlueSqlInjector());
        conf.setDbConfig(dbConfig);

        // 自定义填充策略接口实现
        conf.setMetaObjectHandler(new MybatisPlusMetaObjectHandler());

        return conf;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(GlobalConfig globalConfig) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath*:/com/wegood/**/mapper/**/*Mapper.xml"));

        sqlSessionFactory.setTypeEnumsPackage("com.wegood.**.api.entity");

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);

        if (CollUtil.isNotEmpty(baseTypeHandlers)) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            baseTypeHandlers.forEach(typeHandlerRegistry::register);
        }

        /*
         * MyBatis 自动映射时未知列或未知属性处理策略
         * 通过该配置可指定 MyBatis 在自动映射过程中遇到未知列或者未知属性时如何处理，总共有 3 种可选值：
         * AutoMappingUnknownColumnBehavior.NONE：不做任何处理 (默认值)
         * AutoMappingUnknownColumnBehavior.WARNING：以日志的形式打印相关警告信息
         * AutoMappingUnknownColumnBehavior.FAILING：当作映射失败处理，并抛出异常和详细信息
         */
        configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.WARNING);
        // configuration.setDefaultEnumTypeHandler(EnumAnnotationTypeHandler.class);

        sqlSessionFactory.setConfiguration(configuration);

        // 配置插件
        sqlSessionFactory.setPlugins(new Interceptor[]{
            new PaginationInterceptor().setLimit(PageConst.MAX_PAGE_SIZE),
            new OptimisticLockerInterceptor()
        });
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }

    @NonNull
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}