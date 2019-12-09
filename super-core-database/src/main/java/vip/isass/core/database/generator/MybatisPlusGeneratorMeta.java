package vip.isass.core.database.generator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
public class MybatisPlusGeneratorMeta {

    private DbType dbType;

    private String dataSourceUserName;

    private String dataSourcePassword;

    private String dataSourceUrl;

    private String outputDir;

    private String moduleName;

    private String packageName;

    private String[] tablePrefix;

    private String[] includeTables;

    private String[] excludeTables;

}
