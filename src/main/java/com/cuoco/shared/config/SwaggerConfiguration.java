package com.cuoco.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cuoco API")
                        .description("API para generar recetas de cocina, obtener ingredientes y planificar")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Trabajo práctico integrador - Cuoco")
                                .url("https://www.cuoco.com.ar")
                        )
                );
    }
}