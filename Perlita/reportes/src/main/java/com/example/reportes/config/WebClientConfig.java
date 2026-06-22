package com.example.reportes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicio.productos.url:http://localhost:8082}")
    private String productosUrl;

    @Value("${microservicio.materiales.url:http://localhost:8081}")
    private String materialesUrl;

    @Value("${microservicio.ventas.url:http://localhost:8083}")
    private String ventasUrl;

    @Value("${microservicio.pedidos.url:http://localhost:8084}")
    private String pedidosUrl;

    @Value("${microservicio.gastos.url:http://localhost:8089}")
    private String gastosUrl;

    @Bean("productosWebClient")
    public WebClient productosWebClient() {
        return WebClient.builder().baseUrl(productosUrl).build();
    }

    @Bean("materialesWebClient")
    public WebClient materialesWebClient() {
        return WebClient.builder().baseUrl(materialesUrl).build();
    }

    @Bean("ventasWebClient")
    public WebClient ventasWebClient() {
        return WebClient.builder().baseUrl(ventasUrl).build();
    }

    @Bean("pedidosWebClient")
    public WebClient pedidosWebClient() {
        return WebClient.builder().baseUrl(pedidosUrl).build();
    }

    @Bean("gastosWebClient")
    public WebClient gastosWebClient() {
        return WebClient.builder().baseUrl(gastosUrl).build();
    }
}