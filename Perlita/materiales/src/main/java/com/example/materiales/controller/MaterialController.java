package com.example.materiales.controller;

import com.example.materiales.exception.ResourceNotFoundException;
import com.example.materiales.model.Material;
import com.example.materiales.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiales")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Materiales", description = "Gestión de materiales e inventario")
public class MaterialController {

    @Autowired
    private MaterialService service;

    @GetMapping
    @Operation(summary = "Listar todos los materiales")
    public List<Material> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar material por ID")
    public ResponseEntity<Material> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
    }

    @PostMapping
    @Operation(summary = "Crear un material")
    public ResponseEntity<Material> crear(@RequestBody Material material) {
        return new ResponseEntity<>(service.guardar(material), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un material")
    public ResponseEntity<Material> actualizar(@PathVariable Long id,
                                                @RequestBody Material material) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
        material.setId(id);
        return ResponseEntity.ok(service.guardar(material));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un material")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Listar materiales con stock bajo", description = "Retorna materiales cuyo stock actual está por debajo del mínimo")
    public List<Material> stockBajo() {
        return service.stockBajo();
    }
}