package com.example.qrgenerate.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public OpenAPI infraApi() {

        // General configuration
        var openApi = new OpenAPI();
        openApi.info(new Info()
                        .title("AquaSense Platform API")
                        .description("AquaSense API documentation.")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("AquaSense github page")
                        .url("https://github.com/HorsesDevelopers/backend"));

        // Configurar servidores según el entorno
        if ("prod".equals(activeProfile)) {
            openApi.servers(List.of(
                    new Server()
                            .url("https://qrgenerate-prueba-production.up.railway.app/")
                            .description("Servidor de Producción")
            ));
        } else {
            // Para entorno local u otros
            openApi.servers(List.of(
                    new Server()
                            .url("http://localhost:8080")
                            .description("Servidor Local")
            ));
        }


        return openApi;
    }
}
