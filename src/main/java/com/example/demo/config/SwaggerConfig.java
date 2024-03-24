package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String PROJECT_NAME = "Image transformation and encryption";
    private static final String VERSION_NUMBER = "1.0.1";
    private static final String PROJECT_DESCRIPTION = "Image transformation and encryption - Backend";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title(PROJECT_NAME)
                .version(VERSION_NUMBER)
                .description(PROJECT_DESCRIPTION));
    }
}
