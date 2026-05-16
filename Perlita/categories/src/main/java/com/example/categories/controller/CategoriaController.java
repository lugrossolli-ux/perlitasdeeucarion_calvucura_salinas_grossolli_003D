package com.example.categories.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.categories.model.Categoria;
import com.example.categories.model.ProductoCategoria;
import com.example.categories.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public List<Categoria> listar() {
        return service.listarTodos();
    }

    @GetMapping("/activas")
    public List<Categoria> listarActivas() {
        return service.listarActivas(1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        return new ResponseEntity<>(service.guardar(categoria), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id,
                                                @RequestBody Categoria categoria) {
        return service.buscarPorId(id)
                .map(existente -> {
                    existente.setNombre(categoria.getNombre());
                    existente.setDescripcion(categoria.getDescripcion());
                    existente.setActivo(categoria.getActivo());
                    return ResponseEntity.ok(service.guardar(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/obtenerProducto/{categoriaId}")
    public List<ProductoCategoria> obtenerProductosDe(@PathVariable Long categoriaId) {
        return service.obtenerProductosDe(categoriaId);
    }

    @PatchMapping("/reasignar")
    public ResponseEntity<Object> reasignar(@RequestParam Long origenId, @RequestParam Long destinoId) {
    return service.reasignarProductos(origenId, destinoId);
    }

}