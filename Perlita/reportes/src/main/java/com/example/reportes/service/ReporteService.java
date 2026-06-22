package com.example.reportes.service;

import com.example.reportes.model.Reporte;
import com.example.reportes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final WebClient productosWebClient;
    private final WebClient materialesWebClient;
    private final WebClient ventasWebClient;
    private final WebClient pedidosWebClient;
    private final WebClient gastosWebClient;

    public ReporteService(ReporteRepository reporteRepository,
                          @Qualifier("productosWebClient") WebClient productosWebClient,
                          @Qualifier("materialesWebClient") WebClient materialesWebClient,
                          @Qualifier("ventasWebClient") WebClient ventasWebClient,
                          @Qualifier("pedidosWebClient") WebClient pedidosWebClient,
                          @Qualifier("gastosWebClient") WebClient gastosWebClient) {
        this.reporteRepository = reporteRepository;
        this.productosWebClient = productosWebClient;
        this.materialesWebClient = materialesWebClient;
        this.ventasWebClient = ventasWebClient;
        this.pedidosWebClient = pedidosWebClient;
        this.gastosWebClient = gastosWebClient;
    }

    public List<Reporte> listarTodos() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> buscarPorId(Long id) {
        return reporteRepository.findById(id);
    }

    public Reporte guardar(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public void eliminar(Long id) {
        reporteRepository.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public Reporte generarReporteVentas(LocalDate desde, LocalDate hasta) {
        try {
            List<Map<String, Object>> ventas = ventasWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/ventas")
                            .queryParam("desde", desde)
                            .queryParam("hasta", hasta)
                            .build())
                    .retrieve()
                    .bodyToMono(List.class)
                    .onErrorResume(e -> Mono.empty())
                    .block();

            List<Map<String, Object>> productos = productosWebClient.get()
                    .uri("/api/v1/productos")
                    .retrieve()
                    .bodyToMono(List.class)
                    .onErrorResume(e -> Mono.empty())
                    .block();

            Reporte reporte = new Reporte();
            reporte.setTipo("ventas_por_periodo");
            reporte.setFechaGeneracion(LocalDate.now());
            reporte.setParametros("{\"desde\":\"" + desde + "\",\"hasta\":\"" + hasta + "\"}");
            reporte.setResultadoJson("{\"ventas\":" + (ventas != null ? ventas.size() : 0)
                    + ",\"productos\":" + (productos != null ? productos.size() : 0) + "}");

            return reporteRepository.save(reporte);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de ventas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Reporte generarReporteStockCritico() {
        try {
            List<Map<String, Object>> materiales = materialesWebClient.get()
                    .uri("/api/v1/materiales/stock-bajo")
                    .retrieve()
                    .bodyToMono(List.class)
                    .onErrorResume(e -> Mono.empty())
                    .block();

            Reporte reporte = new Reporte();
            reporte.setTipo("stock_critico");
            reporte.setFechaGeneracion(LocalDate.now());
            reporte.setParametros("{}");
            reporte.setResultadoJson("{\"materialesStockBajo\":"
                    + (materiales != null ? materiales.size() : 0) + "}");

            return reporteRepository.save(reporte);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de stock crítico: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Reporte generarReportePedidos() {
        try {
            List<Map<String, Object>> pedidos = pedidosWebClient.get()
                    .uri("/api/v1/pedidos")
                    .retrieve()
                    .bodyToMono(List.class)
                    .onErrorResume(e -> Mono.empty())
                    .block();

            Reporte reporte = new Reporte();
            reporte.setTipo("estado_pedidos");
            reporte.setFechaGeneracion(LocalDate.now());
            reporte.setParametros("{}");
            reporte.setResultadoJson("{\"totalPedidos\":" + (pedidos != null ? pedidos.size() : 0) + "}");

            return reporteRepository.save(reporte);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de pedidos: " + e.getMessage());
        }
    }
}