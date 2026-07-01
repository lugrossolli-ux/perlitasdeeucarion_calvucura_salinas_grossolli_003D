package com.example.ventas.service;

import com.example.ventas.model.Venta;
import com.example.ventas.repository.VentaProductoRepository;
import com.example.ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaProductoRepository ventaProductoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public List<Venta> buscarPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }

    public Venta crearVenta(Venta venta) {
        venta.setFecha(LocalDate.now());

        if (venta.getProductos() != null) {
            double total = venta.getProductos().stream()
                    .mapToDouble(p -> p.getPrecioUnitario() * p.getCantidad())
                    .sum();
            venta.setTotal(total);

            Venta guardada = ventaRepository.save(venta);
            venta.getProductos().forEach(p -> p.setVenta(guardada));
            ventaProductoRepository.saveAll(venta.getProductos());

            return enriquecerConProductos(guardada);
        }

        return ventaRepository.save(venta);
    }

    public Venta obtenerVentaCompleta(Long id) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta == null) return null;
        return enriquecerConProductos(venta);
    }

    private Venta enriquecerConProductos(Venta venta) {
        if (venta.getProductos() == null) return venta;

        venta.getProductos().forEach(vp -> {
            try {
                Object producto = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8082/productos/" + vp.getProductoId())
                        .retrieve()
                        .bodyToMono(Object.class)
                        .block();
                vp.setDatosProducto(producto);
            } catch (Exception e) {
                vp.setDatosProducto("Producto no disponible");
            }
        });

        return venta;
    }

    public List<Map<String, Object>> resumenPorMetodoPago() {
    List<Object[]> raw = ventaRepository.resumenPorMetodoPago();
    List<Map<String, Object>> resultado = new ArrayList<>();
    for (Object[] fila : raw) {
        Map<String, Object> item = new HashMap<>();
        item.put("metodoPago", fila[0]);
        item.put("cantidadVentas", fila[1]);
        item.put("totalRecaudado", fila[2]);
        resultado.add(item);
    }
    return resultado;
}
}