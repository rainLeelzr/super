# 部署文档

- 本程序是@service-name-cn@，基于 java 开发。
- 本部署包是单体包，已集成其他依赖的微服务

## 前置条件

- 已安装 jdk8 或以上

- 已有 mysql5.7 或以上

## 创建数据库

- 启动服务时，程序会自动判断数据库是否存在，不存在则自动创建数据库。数据库名读取 `spring.datasource.mysql.master.database` 的配置

- 如需更改数据库名，请修改参数 `spring.datasource.mysql.master.database`

```

## 拷贝文件

- 在服务器创建文件夹：/opt/@service-name@
- 若服务器已存在部署包，建议备份原部署包
- 将压缩包 @project.artifactId@-bin.zip 拷贝到服务器 /opt/@service-name@
- 进入服务器文件夹 cd /opt/@service-name@
- 解压文件。解压文件前，请认真查看服务器已有配置文件与压缩包内文件，不要在解压时，覆盖掉已有配置文件
- unzip @project.artifactId@-bin.zip

## 修改参数

- 首次部署时，根据现场实际情况修改 application.properties 文件的参数

```
vi application.properties

spring.datasource.mysql.master.host=127.0.0.1
spring.datasource.mysql.master.port=3306
spring.datasource.mysql.master.username=root
spring.datasource.mysql.master.password=123456
spring.datasource.mysql.master.database=@service-name@
```

## 启动服务

- linux系统 ./start.sh
- windows系统 双击winstart.bat

## 查看日志

- linux 环境启动服务后，会自动使用 tail 命令打开日志

- 输出以下日志，视为启动成功
``` text
Tomcat started on port(s): 20000 (http) with context path ''
Started MessageSpringbootApp in 20.82 seconds (JVM running for 21.431)
```

## 停止服务

- linux系统 直接 ps -ef|grep java 找到对应的进程，再 kill 即可
- windows系统 关闭命令行窗口