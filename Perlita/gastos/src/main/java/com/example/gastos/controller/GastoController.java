package com.example.gastos.controller;

import com.example.gastos.exception.ResourceNotFoundException;
import com.example.gastos.model.Gasto;
import com.example.gastos.service.GastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/gastos")
public class GastoController {

    @Autowired
    private GastoService service;

    @GetMapping
    public List<Gasto> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gasto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id: " + id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Gasto> listarPorCategoria(@PathVariable Long categoriaId) {
        return service.listarPorCategoria(categoriaId);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public List<Gasto> listarPorProveedor(@PathVariable Long proveedorId) {
        return service.listarPorProveedor(proveedorId);
    }

    @PostMapping
    public ResponseEntity<Gasto> crear(@RequestBody Gasto gasto) {
        return new ResponseEntity<>(service.guardar(gasto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gasto> actualizar(@PathVariable Long id,
                                             @RequestBody Gasto gasto) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id: " + id));
        gasto.setId(id);
        return ResponseEntity.ok(service.guardar(gasto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}