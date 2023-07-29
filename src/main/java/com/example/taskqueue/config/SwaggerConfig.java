package com.example.taskqueue.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${domain.name}")
    private String host;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("task queue")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server().url(host);
        return new OpenAPI()
                .addServersItem(server)
                .info(new Info()
                        .title("Task Queue API")
                        .description("Task Queue 프로젝트 API 명세서입니다.")
                        .version("v1"));
//                .components(new Components().addSecuritySchemes("Bearer-Key",
//                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer").bearerFormat("JWT"))
//                );
    }

}
