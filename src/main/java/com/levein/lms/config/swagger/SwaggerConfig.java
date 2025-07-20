package com.levein.lms.config.swagger;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi apiV1() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(openApi -> openApi.info(
                        new Info().title("Library Management API")
                                .version("v1")
                                .description("API documentation for Library Management System V1")))
                .build();
    }

    @Bean
    public GroupedOpenApi apiV2() {
        return GroupedOpenApi.builder()
                .group("v2")
                .pathsToMatch("/api/v2/**")
                .addOpenApiCustomizer(openApi -> openApi.info(
                        new Info().title("Library Management API")
                                .version("v2")
                                .description("API documentation for Library Management System version 2")))
                .build();
    }
}
