package com.example.gastos.controller;

import com.example.gastos.exception.ResourceNotFoundException;
import com.example.gastos.model.CategoriaGasto;
import com.example.gastos.service.CategoriaGastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categorias-gasto")
public class CategoriaGastoController {

    @Autowired
    private CategoriaGastoService service;

    @GetMapping
    public List<CategoriaGasto> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaGasto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de gasto no encontrada con id: " + id));
    }

    @PostMapping
    public ResponseEntity<CategoriaGasto> crear(@RequestBody CategoriaGasto categoria) {
        return new ResponseEntity<>(service.guardar(categoria), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaGasto> actualizar(@PathVariable Long id,
                                                      @RequestBody CategoriaGasto categoria) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de gasto no encontrada con id: " + id));
        categoria.setId(id);
        return ResponseEntity.ok(service.guardar(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de gasto no encontrada con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}