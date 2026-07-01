package com.example.gastos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicio.proveedores.url:http://localhost:8086}")
    private String proveedoresUrl;

    @Bean("proveedoresWebClient")
    public WebClient proveedoresWebClient() {
        return WebClient.builder().baseUrl(proveedoresUrl).build();
    }
}
