package com.example.pedidos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    public Pedido guardar(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        }
        return repository.save(pedido);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Pedido> listarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    private static final Map<String, String> TRANSICIONES_VALIDAS = Map.of(
        "pendiente",  "en_proceso",
        "en_proceso", "completado"
    );

    public ResponseEntity<Object> cambiarEstado(Long id, String nuevoEstado) {
        return repository.findById(id).map(pedido -> {
            String estadoActual = pedido.getEstado();
            String siguiente = TRANSICIONES_VALIDAS.get(estadoActual);

            if (siguiente == null || !siguiente.equals(nuevoEstado)) {
                return ResponseEntity
                    .badRequest()
                    .body((Object) ("Transición inválida: " + estadoActual + " → " + nuevoEstado
                        + ". Solo se permite: " + estadoActual + " → "
                        + (siguiente != null ? siguiente : "ninguno (ya está completado)")));
            }

            pedido.setEstado(nuevoEstado);
            return ResponseEntity.ok((Object) repository.save(pedido));
        }).orElse(ResponseEntity.notFound().build());
    }
}