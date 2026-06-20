package com.example.categories.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.categories.exception.ResourceNotFoundException;
import com.example.categories.model.Categoria;
import com.example.categories.model.ProductoCategoria;
import com.example.categories.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
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

    @Operation(summary = "Buscar una categoría por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        return new ResponseEntity<>(service.guardar(categoria), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody Categoria categoria) {
        return service.buscarPorId(id)
                .map(existente -> {
                    existente.setNombre(categoria.getNombre());
                    existente.setDescripcion(categoria.getDescripcion());
                    existente.setActivo(categoria.getActivo());
                    return ResponseEntity.ok(service.guardar(existente));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
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