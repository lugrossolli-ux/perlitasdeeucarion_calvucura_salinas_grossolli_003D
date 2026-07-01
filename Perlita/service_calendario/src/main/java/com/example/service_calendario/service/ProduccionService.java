package com.example.service_calendario.service;

import com.example.service_calendario.exception.ResourceNotFoundException;
import com.example.service_calendario.model.Bloqueo;
import com.example.service_calendario.model.Produccion;
import com.example.service_calendario.repository.BloqueoRepository;
import com.example.service_calendario.repository.ProduccionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProduccionService {

    private final ProduccionRepository produccionRepository;
    private final BloqueoRepository bloqueoRepository;
    private final WebClient productosWebClient;
    private final WebClient pedidosWebClient;

    public ProduccionService(ProduccionRepository produccionRepository,
                              BloqueoRepository bloqueoRepository,
                              @Qualifier("productosWebClient") WebClient productosWebClient,
                              @Qualifier("pedidosWebClient") WebClient pedidosWebClient) {
        this.produccionRepository = produccionRepository;
        this.bloqueoRepository = bloqueoRepository;
        this.productosWebClient = productosWebClient;
        this.pedidosWebClient = pedidosWebClient;
    }

    public List<Produccion> listarTodas() {
        List<Produccion> lista = produccionRepository.findAll();
        lista.forEach(this::enriquecerProduccion);
        return lista;
    }

    public Produccion buscarPorId(Integer id) {
        Produccion produccion = produccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producción no encontrada con id: " + id));
        enriquecerProduccion(produccion);
        return produccion;
    }

    public Produccion crear(Produccion produccion) {
        if (produccion.getFechaFinEstimada().isBefore(produccion.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha fin estimada no puede ser anterior a la fecha de inicio");
        }
        validarSinSolapeConBloqueos(produccion.getFechaInicio(), produccion.getFechaFinEstimada());
        return produccionRepository.save(produccion);
    }

    public Produccion actualizar(Integer id, Produccion datos) {
        Produccion existente = produccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producción no encontrada con id: " + id));

        existente.setPedidoId(datos.getPedidoId());
        existente.setProductoId(datos.getProductoId());
        existente.setFechaInicio(datos.getFechaInicio());
        existente.setFechaFinEstimada(datos.getFechaFinEstimada());
        existente.setFechaFinReal(datos.getFechaFinReal());
        existente.setEstado(datos.getEstado());
        existente.setNotas(datos.getNotas());

        return produccionRepository.save(existente);
    }

    private static final Map<String, Set<String>> TRANSICIONES_PROD = Map.of(
        "programado",  Set.of("en_proceso", "cancelado"),
        "en_proceso",  Set.of("finalizado", "cancelado"),
        "finalizado",  Set.of(),
        "cancelado",   Set.of()
    );

    public Produccion cambiarEstado(Integer id, String nuevoEstado) {
        Produccion existente = produccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producción no encontrada con id: " + id));

        String actual = existente.getEstado();
        Set<String> permitidos = TRANSICIONES_PROD.get(actual);

        if (permitidos == null || !permitidos.contains(nuevoEstado)) {
            throw new IllegalArgumentException(
                "Transición inválida: " + actual + " → " + nuevoEstado);
        }

        existente.setEstado(nuevoEstado);

        if ("finalizado".equalsIgnoreCase(nuevoEstado)) {
            existente.setFechaFinReal(LocalDate.now());
        }

        return produccionRepository.save(existente);
    }

    public void eliminar(Integer id) {
        if (!produccionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producción no encontrada con id: " + id);
        }
        produccionRepository.deleteById(id);
    }

    public List<Produccion> obtenerCalendario(LocalDate desde, LocalDate hasta) {
        List<Produccion> lista = produccionRepository.findByFechaInicioBetween(desde, hasta);
        lista.forEach(this::enriquecerProduccion);
        return lista;
    }

    private void validarSinSolapeConBloqueos(LocalDate inicio, LocalDate fin) {
        List<Bloqueo> bloqueos = bloqueoRepository
                .findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(fin, inicio);

        if (!bloqueos.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede programar producción: existe un bloqueo en ese rango de fechas (" +
                            bloqueos.get(0).getMotivo() + ")");
        }
    }

    private void enriquecerProduccion(Produccion produccion) {
        try {
            Map<?, ?> producto = productosWebClient.get()
                    .uri("/productos/{id}", produccion.getProductoId())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorResume(e -> Mono.empty())
                    .block();

            produccion.setDatosProducto(producto);
        } catch (Exception e) {
            produccion.setDatosProducto(null);
        }

        if (produccion.getPedidoId() != null) {
            try {
                Map<?, ?> pedido = pedidosWebClient.get()
                        .uri("/pedidos/{id}", produccion.getPedidoId())
                        .retrieve()
                        .bodyToMono(Map.class)
                        .onErrorResume(e -> Mono.empty())
                        .block();

                produccion.setDatosPedido(pedido);
            } catch (Exception e) {
                produccion.setDatosPedido(null);
            }
        }
    }
}