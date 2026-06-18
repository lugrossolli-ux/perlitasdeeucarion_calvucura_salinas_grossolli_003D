package com.example.productos.controller;

import com.example.productos.exception.ResourceNotFoundException;
import com.example.productos.model.Producto;
import com.example.productos.model.ProductoMaterial;
import com.example.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public List<Producto> listar() {
        return service.listarTodos();
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar productos activos")
    public List<Producto> listarActivos() {
        return service.listarActivos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @PostMapping
    @Operation(summary = "Crear un producto")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return new ResponseEntity<>(service.guardar(producto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
                                                @RequestBody Producto producto) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        producto.setId(id);
        return ResponseEntity.ok(service.guardar(producto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/materiales")
    @Operation(summary = "Ver materiales de un producto")
    public List<ProductoMaterial> verMateriales(@PathVariable Long id) {
        return service.obtenerMateriales(id);
    }

    @PostMapping("/{id}/materiales")
    @Operation(summary = "Agregar material a un producto")
    public ResponseEntity<ProductoMaterial> agregarMaterial(
            @PathVariable Long id,
            @RequestBody ProductoMaterial pm) {
        return service.buscarPorId(id)
                .map(producto -> {
                    pm.setProducto(producto);
                    return new ResponseEntity<>(service.agregarMaterial(pm), HttpStatus.CREATED);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }
}