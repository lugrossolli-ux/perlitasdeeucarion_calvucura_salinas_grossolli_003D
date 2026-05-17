package com.example.materiales.controller;

import com.example.materiales.exception.ResourceNotFoundException;
import com.example.materiales.model.Material;
import com.example.materiales.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/materiales")
public class MaterialController {

    @Autowired
    private MaterialService service;

    @GetMapping
    public List<Material> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
    }

    @PostMapping
    public ResponseEntity<Material> crear(@RequestBody Material material) {
        return new ResponseEntity<>(service.guardar(material), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> actualizar(@PathVariable Long id,
                                                @RequestBody Material material) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
        material.setId(id);
        return ResponseEntity.ok(service.guardar(material));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock-bajo")
    public List<Material> stockBajo() {
        return service.stockBajo();
    }
}