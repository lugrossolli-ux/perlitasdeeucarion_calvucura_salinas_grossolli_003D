package com.example.productos.service;

import com.example.productos.model.Producto;
import com.example.productos.model.ProductoMaterial;
import com.example.productos.repository.ProductoMaterialRepository;
import com.example.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMaterialRepository productoMaterialRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public List<ProductoMaterial> obtenerMateriales(Long productoId) {
        List<ProductoMaterial> materiales = productoMaterialRepository
                .findByProductoId(productoId);
        materiales.forEach(pm -> {
            try {
                Object material = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8081/materiales/" + pm.getMaterialId())
                        .retrieve()
                        .bodyToMono(Object.class)
                        .block();
                pm.setDatosMaterial(material);
            } catch (Exception e) {
                pm.setDatosMaterial("Material no disponible");
            }
        });
        return materiales;
    }

    public ProductoMaterial agregarMaterial(ProductoMaterial pm) {
        return productoMaterialRepository.save(pm);
    }
}