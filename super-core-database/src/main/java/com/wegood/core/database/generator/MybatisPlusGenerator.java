package com.wegood.core.database.generator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.extension.toolkit.PackageHelper;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.wegood.core.entity.LogicDeleteEntity;
import com.wegood.core.entity.TimeTracedEntity;
import com.wegood.core.entity.UserTracedEntity;
import com.wegood.core.entity.VersionEntity;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class MybatisPlusGenerator {

    private static final String PREFIX = "v1";

    /**
     * 需要覆盖的文件
     */
    public static final Pattern[] FILE_OVERRIDE_PATTERNS = new Pattern[]{
        Pattern.compile(".*V1\\w*Controller.java$"),
        Pattern.compile(".*V1\\w*Service.java$"),
        Pattern.compile(".*V1\\w*Repository.java$"),
        Pattern.compile(".*V1\\w*Mapper.java$"),
        Pattern.compile(".*V1\\w*Mapper.xml$"),
        Pattern.compile(".*/api/entity.*"),
        Pattern.compile(".*/api/criteria.*"),
        Pattern.compile(".*V1\\w*ServiceTest.java$"),
        Pattern.compile(".*V1\\w*TestSuite.java$"),
        Pattern.compile(".*/initDb.sql$")
    };

    public static void main(String[] args) {
    }

    @SneakyThrows
    public static void generate(MybatisPlusGeneratorMeta meta) {
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
            .setDbType(meta.getDbType())
            .setUrl(meta.getDataSourceUrl())
            .setUsername(meta.getDataSourceUserName())
            .setPassword(meta.getDataSourcePassword())
            .setTypeConvert((ITypeConvert) Class.forName("com.wegood.core.database.postgresql.convert.PostgreSqlTypeConvert").newInstance());
        switch (meta.getDbType()) {
            case MYSQL:
                dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
                break;
            case MARIADB:
                break;
            case ORACLE:
                break;
            case DB2:
                break;
            case H2:
                break;
            case HSQL:
                break;
            case SQLITE:
                break;
            case POSTGRE_SQL:
                dataSourceConfig.setDriverName("org.postgresql.Driver");
                break;
            case SQL_SERVER2005:
                break;
            case SQL_SERVER:
                break;
            case DM:
                break;
            case OTHER:
                break;
            default:
        }

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig()
            // 是否跳过视图
            .setSkipView(true)

            // 是否大写命名
            .setCapitalMode(true)

            // 是否为lombok模型
            .setEntityLombokModel(true)

            // 是否生成字段常量
            .setEntityColumnConstant(true)

            // 数据库表映射到实体的命名策略
            .setNaming(NamingStrategy.underline_to_camel)

            // 数据库表字段映射到实体的命名策略
            .setColumnNaming(NamingStrategy.underline_to_camel)

            // 表前缀
            .setTablePrefix(meta.getTablePrefix())

            // 需要包含的表名，允许正则表达式（与exclude二选一配置）
            .setInclude(meta.getIncludeTables())

            // 需要排除的表名，允许正则表达式
            .setExclude(meta.getExcludeTables())

            // 乐观锁属性名称
            .setVersionFieldName(VersionEntity.VERSION)

            // 逻辑删除属性名称
            .setLogicDeleteFieldName(LogicDeleteEntity.DELETE_FLAG)

            // 需要自动填充的字段
            .setTableFillList(Arrays.asList(
                new TableFill(VersionEntity.VERSION, FieldFill.INSERT),
                new TableFill(TimeTracedEntity.CREATED_TIME, FieldFill.INSERT),
                new TableFill(TimeTracedEntity.MODIFY_TIME, FieldFill.INSERT_UPDATE),
                new TableFill(UserTracedEntity.CREATE_USER_ID, FieldFill.INSERT),
                new TableFill(UserTracedEntity.CREATE_USER_NAME, FieldFill.INSERT),
                new TableFill(UserTracedEntity.MODIFY_USER_ID, FieldFill.INSERT_UPDATE),
                new TableFill(UserTracedEntity.MODIFY_USER_NAME, FieldFill.INSERT_UPDATE),
                new TableFill(LogicDeleteEntity.DELETE_FLAG, FieldFill.INSERT))
            );

        // 全局配置
        GlobalConfig config = new GlobalConfig()
            .setActiveRecord(false)
            .setEnableCache(false)
            .setAuthor("wegood")
            .setControllerName("%sController")
            .setServiceName("%sService")
            .setMapperName("%sMapper")
            .setBaseColumnList(true)
            .setBaseResultMap(true)
            .setOutputDir(meta.getOutputDir() + "/src/main/java")
            .setOpen(false)
            .setFileOverride(true);

        BeansWrapper wrapper = new BeansWrapperBuilder(new Version("2.3.28")).build();
        TemplateHashModel staticModels = wrapper.getStaticModels();


        // 自定义新模板
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(new HashMap<String, Object>(16) {{
                    put("moduleName", meta.getModuleName());
                    put("package", meta.getPackageName());
                    put("criteriaPackageName", meta.getPackageName() + "." + meta.getModuleName() + ".api.criteria");
                    put("feignPackage", meta.getPackageName() + "." + meta.getModuleName() + ".api.feign.client");
                    put("tablePrefix", meta.getTablePrefix());
                    put("prefix", PREFIX);

                    try {
                        TemplateHashModel idEntity = (TemplateHashModel) staticModels.get("com.wegood.core.entity.IdEntity");
                        put("idEntity", idEntity);
                        TemplateHashModel chainedEntity = (TemplateHashModel) staticModels.get("com.wegood.core.entity.ChainedEntity");
                        put("chainedEntity", chainedEntity);
                        TemplateHashModel userTracedEntity = (TemplateHashModel) staticModels.get("com.wegood.core.entity.UserTracedEntity");
                        put("userTracedEntity", userTracedEntity);
                        TemplateHashModel timeTracedEntity = (TemplateHashModel) staticModels.get("com.wegood.core.entity.TimeTracedEntity");
                        put("timeTracedEntity", timeTracedEntity);
                        TemplateHashModel versionEntity = (TemplateHashModel) staticModels.get("com.wegood.core.entity.VersionEntity");
                        put("versionEntity", versionEntity);
                        TemplateHashModel logicDeleteEntity = (TemplateHashModel) staticModels.get("com.wegood.core.entity.LogicDeleteEntity");
                        put("logicDeleteEntity", logicDeleteEntity);
                    } catch (TemplateModelException e) {
                        e.printStackTrace();
                    }
                }});
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.addAll(CollUtil.newArrayList(
            new FileOutConfig("/templates/" + PREFIX + "Repository.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    String path = config.getOutputDir() +
                        "/" + meta.getPackageName().replaceAll("\\.", "/") +
                        "/" + meta.getModuleName() +
                        "/" + PREFIX + "/repository/";
                    return path + StrUtil.upperFirst(PREFIX) + tableInfo.getEntityName() + "MpRepository.java";
                }
            }
            , new FileOutConfig("/templates/repository.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    String path = config.getOutputDir() +
                        "/" + meta.getPackageName().replaceAll("\\.", "/") +
                        "/" + meta.getModuleName() +
                        "/repository/";
                    return path + tableInfo.getEntityName() + "MpRepository.java";
                }
            }, new FileOutConfig("/templates/criteria.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    String path = config.getOutputDir() +
                        "/" + meta.getPackageName().replaceAll("\\.", "/") +
                        "/" + meta.getModuleName() +
                        "/api/criteria/";
                    return path + tableInfo.getEntityName() + "Criteria.java";
                }
            }, new FileOutConfig("/templates/" + PREFIX + "Controller.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    String path = config.getOutputDir() +
                        "/" + meta.getPackageName().replaceAll("\\.", "/") +
                        "/" + meta.getModuleName() +
                        "/" + PREFIX + "/controller/";
                    return path + StrUtil.upperFirst(PREFIX) + tableInfo.getEntityName() + "Controller.java";
                }
                // feign name 不能重名，每张表生成的feign client 启动报错
                // }, new FileOutConfig("/templates/feignClient.java.ftl") {
                //     @Override
                //     public String outputFile(TableInfo tableInfo) {
                //         // 自定义输入文件名称
                //         String path = config.getOutputDir() +
                //             "/" + meta.getPackageName().replaceAll("\\.", "/") +
                //             "/" + meta.getModuleName() +
                //             "/api/feign/client/";
                //         return path + tableInfo.getEntityName() + "FeignClient.java";
                //     }
            }, new FileOutConfig("/templates/" + PREFIX + "Service.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    String path = config.getOutputDir() +
                        "/" + meta.getPackageName().replaceAll("\\.", "/") +
                        "/" + meta.getModuleName() +
                        "/" + PREFIX + "/service/";
                    return path + StrUtil.upperFirst(PREFIX) + tableInfo.getEntityName() + "Service.java";
                }
            }, new FileOutConfig("/templates/" + PREFIX + "Mapper.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    String path = config.getOutputDir() +
                        "/" + meta.getPackageName().replaceAll("\\.", "/") +
                        "/" + meta.getModuleName() +
                        "/" + PREFIX + "/mapper/";
                    return path + StrUtil.upperFirst(PREFIX) + tableInfo.getEntityName() + "Mapper.java";
                }
            }, new FileOutConfig("/templates/" + PREFIX + "Mapper.xml.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    String path = config.getOutputDir() +
                        "/" + meta.getPackageName().replaceAll("\\.", "/") +
                        "/" + meta.getModuleName() +
                        "/" + PREFIX + "/mapper/xml/";
                    return path + StrUtil.upperFirst(PREFIX) + tableInfo.getEntityName() + "Mapper.xml";
                }
                // }, new FileOutConfig("/templates/" + PREFIX + "ServiceTest.java.vm") {
                //     @Override
                //     public String outputFile(TableInfo tableInfo) {
                //         // 自定义输入文件名称
                //         String path = config.getOutputDir() +
                //             "/" + meta.getPackageName().replaceAll("\\.", "/") +
                //             "/" + meta.getModuleName() +
                //             "/auto/service/";
                //         path = path.replaceAll("\\\\", "/").replace("/src/main/java", "/src/test/java");
                //         return path + CAPITAL_PREFIX + tableInfo.getServiceName() + "Test.java";
                //     }
                // }, new FileOutConfig("/templates/" + PREFIX + "TestSuite.java.vm") {
                //     @Override
                //     public String outputFile(TableInfo tableInfo) {
                //         // 自定义输入文件名称
                //         String path = config.getOutputDir() +
                //             "/" + meta.getPackageName().replaceAll("\\.", "/") +
                //             "/" + meta.getModuleName() +
                //             "/";
                //         path = path.replaceAll("\\\\", "/").replace("/src/main/java", "/src/test/java");
                //         return path + CAPITAL_PREFIX + StrUtil.upperFirst(meta.getModuleName()) + "TestSuite.java";
                //     }
                // }, new FileOutConfig("/templates/testSuite.java.vm") {
                //     @Override
                //     public String outputFile(TableInfo tableInfo) {
                //         // 自定义输入文件名称
                //         String path = config.getOutputDir() +
                //             "/" + meta.getPackageName().replaceAll("\\.", "/") +
                //             "/";
                //         path = path.replaceAll("\\\\", "/").replace("/src/main/java", "/src/test/java");
                //         return path + "TestSuite.java";
                //     }
                // }, new FileOutConfig("/templates/initDb.sql.vm") {
                //     @Override
                //     public String outputFile(TableInfo tableInfo) {
                //         // 自定义输入文件名称
                //         String path = config.getOutputDir() +
                //             "/";
                //         path = path.replaceAll("\\\\", "/").replace("/src/main/java", "/src/main/resources/script");
                //         return path + "initDb.sql";
                //     }
            }
        ));
        cfg.setFileOutConfigList(focList);

        cfg.setFileCreate((configBuilder, fileType, filePath) -> {
            filePath = filePath.replaceAll("\\\\", "/");
            File file = new File(filePath);
            boolean exist = file.exists();
            if (exist && configBuilder.getGlobalConfig().isFileOverride()) {
                for (Pattern pattern : FILE_OVERRIDE_PATTERNS) {
                    if (pattern.matcher(filePath).matches()) {
                        log.info(filePath + " --> {}", true);
                        return true;
                    }
                }
                log.info(filePath + " --> {}", false);
                // return false;
                return true;
            } else {
                PackageHelper.mkDir(file.getParentFile());
                return true;
            }
        });

        new AutoGenerator()
            .setGlobalConfig(config)
            .setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig)
            .setPackageInfo(
                new PackageConfig()
                    .setParent(meta.getPackageName())
                    .setMapper("mapper")
                    .setXml("mapper.xml")
                    .setEntity("api.entity")
                    .setService("service")
                    .setModuleName(meta.getModuleName())
            )
            .setTemplate(
                new TemplateConfig()
                    .setServiceImpl(null)
            )
            .setCfg(cfg)
            .setTemplateEngine(new FreemarkerTemplateEngine())
            .execute();
    }
}