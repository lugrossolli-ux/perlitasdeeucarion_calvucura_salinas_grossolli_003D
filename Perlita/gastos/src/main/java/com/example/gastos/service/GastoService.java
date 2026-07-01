package com.example.gastos.service;

import com.example.gastos.exception.ResourceNotFoundException;
import com.example.gastos.model.Gasto;
import com.example.gastos.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GastoService {
    private final GastoRepository repository;
    private final WebClient proveedoresWebClient;

    public GastoService(GastoRepository repository,
                        @Qualifier("proveedoresWebClient") WebClient proveedoresWebClient) {
        this.repository = repository;
        this.proveedoresWebClient = proveedoresWebClient;
    }

    public List<Gasto> listarTodos() {
        return repository.findAll();
    }

    public List<Gasto> listarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    public List<Gasto> listarPorProveedor(Long proveedorId) {
        return repository.findByProveedorId(proveedorId);
    }

    public Optional<Gasto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Gasto guardar(Gasto gasto) {
        if (gasto.getProveedorId() != null) {
            Boolean existe = proveedoresWebClient.get()
                    .uri("/proveedores/{id}", gasto.getProveedorId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .map(r -> true)
                    .onErrorResume(e -> Mono.just(false))
                    .block();
            if (existe == null || !existe) {
                throw new ResourceNotFoundException("Proveedor no encontrado con id: " + gasto.getProveedorId());
            }
        }
        if (gasto.getFecha() == null) {
            gasto.setFecha(LocalDate.now());
        }
        return repository.save(gasto);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
