package com.example.productos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Perlitas – Servicio de Productos")
                        .version("1.0")
                        .description("Gestión de productos y relaciones con materiales"))
                .servers(List.of(
                        new Server().url("http://localhost:9090").description("Gateway")
                ));
    }
}