package ventas.service;

import ventas.model.Venta;
import ventas.model.VentaProducto;
import ventas.repository.VentaProductoRepository;
import ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.util.List;
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
}