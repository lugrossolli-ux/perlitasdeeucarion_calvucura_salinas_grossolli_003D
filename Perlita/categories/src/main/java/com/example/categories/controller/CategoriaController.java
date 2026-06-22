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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public List<Categoria> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Listar categorías activas")
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

    @Operation(summary = "Crear una nueva categoría")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        return new ResponseEntity<>(service.guardar(categoria), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
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

    @Operation(summary = "Eliminar una categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener productos de una categoría")
    @GetMapping("/obtenerProducto/{categoriaId}")
    public List<ProductoCategoria> obtenerProductosDe(@PathVariable Long categoriaId) {
        return service.obtenerProductosDe(categoriaId);
    }

    @Operation(summary = "Reasignar productos entre categorías",
               description = "Transfiere todos los productos de la categoría origen a la categoría destino")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos reasignados"),
            @ApiResponse(responseCode = "400", description = "Categoría destino inactiva")
    })
    @PatchMapping("/reasignar")
    public ResponseEntity<Object> reasignar(@RequestParam Long origenId, @RequestParam Long destinoId) {
        return service.reasignarProductos(origenId, destinoId);
    }
}
