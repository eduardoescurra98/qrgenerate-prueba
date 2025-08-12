package com.example.qrgenerate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Generación de QR")
                        .version("1.0")
                        .description("API REST para generar códigos QR a partir de texto")
                        .contact(new Contact()
                                .name("Eduardo Escurra")
                                .email("eduardoescurra98@example.com")));
    }
}