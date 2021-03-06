package com.tybest.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author tb
 * @date 2018/12/3 11:37
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String BASE_PACKAGE = "com.tybest.seckill.controller";

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("秒杀案例")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any()).build();
    }

    /**
     * 预览地址:swagger-ui.html
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("秒杀案例接口文档").termsOfServiceUrl("https://tybest.com")
                .contact(new Contact("TYBEST ", "https://www.tybest.com/", "331344988@qq.com")).version("1.1").build();
    }
}
