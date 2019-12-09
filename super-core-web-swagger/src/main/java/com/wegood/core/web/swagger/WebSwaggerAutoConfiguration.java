package com.wegood.core.web.swagger;

import cn.hutool.core.collection.CollUtil;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;


/**
 * @author Rain
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class WebSwaggerAutoConfiguration {

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.wegood"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(CollUtil.newArrayList(
                SecurityContext.builder()
                    .securityReferences(authorization())
                    .build()
            ))
            .securitySchemes(CollUtil.newArrayList(
                apiKey()
            ));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("微好店api文档")
            .description("本文档聚合各微服务的接口文档，文档内容根据代码自动生成。")
            .termsOfServiceUrl("https://ms.wegood.shop/apidoc/doc.html?plus=1&cache=1&lang=zh")
            .contact(new Contact("rain", "https://www.wegood.shop", "lizhirong100@163.com"))
            .version("1.0")
            .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("jwt", "Authorization", "header");
    }

    private List<SecurityReference> authorization() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{
            new AuthorizationScope("global", "jwt token")
        };
        return Lists.newArrayList(new SecurityReference("jwt", authorizationScopes));
    }

}
