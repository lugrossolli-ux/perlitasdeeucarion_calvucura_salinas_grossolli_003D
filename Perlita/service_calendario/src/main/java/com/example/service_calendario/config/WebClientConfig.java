package com.example.service_calendario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicio.productos.url}")
    private String productosUrl;

    @Value("${microservicio.pedidos.url}")
    private String pedidosUrl;

    @Bean
    public WebClient productosWebClient() {
        return WebClient.builder()
                .baseUrl(productosUrl)
                .build();
    }

    @Bean
    public WebClient pedidosWebClient() {
        return WebClient.builder()
                .baseUrl(pedidosUrl)
                .build();
    }
}