package com.example.categories.service;

import java.util.Map;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.categories.model.Categoria;
import com.example.categories.model.ProductoCategoria;
import com.example.categories.repository.CategoriaRepository;
import com.example.categories.repository.ProductoCategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoCategoriaRepository productoCategoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarActivas(Integer activo) {
        return categoriaRepository.findByActivo(activo);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    public List<ProductoCategoria> obtenerProductosDe(Long categoriaId) {
        return productoCategoriaRepository.findByCategoria_Id(categoriaId);
    }

    public ResponseEntity<Object> reasignarProductos(Long origenId, Long destinoId) {
    Categoria origen = categoriaRepository.findById(origenId).orElse(null);
    Categoria destino = categoriaRepository.findById(destinoId).orElse(null);

    if (origen == null || destino == null) {
        return ResponseEntity.badRequest().body("Una o ambas categorías no existen.");
    }

    if (destino.getActivo() == 0) {
        return ResponseEntity.badRequest().body("La categoría destino está inactiva.");
    }

    int cantidad = productoCategoriaRepository.reasignarProductos(origenId, destinoId);

    Map<String, Object> respuesta = new HashMap<>();
    respuesta.put("productosReasignados", cantidad);
    respuesta.put("categoriaOrigen", origen.getNombre());
    respuesta.put("categoriaDestino", destino.getNombre());
    return ResponseEntity.ok(respuesta);
    }
}

