package com.example.galeria.controller;

import com.example.galeria.model.Galeria;
import com.example.galeria.service.GaleriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/galeria")
public class GaleriaController {

    @Autowired
    private GaleriaService galeriaService;

    @GetMapping
    public List<Galeria> listarTodos() {
        return galeriaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Galeria> buscarPorId(@PathVariable Long id) {
        return galeriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{productoId}")
    public List<Galeria> buscarPorProducto(@PathVariable Long productoId) {
        return galeriaService.buscarPorProducto(productoId);
    }

    @PostMapping
    public Galeria guardar(@RequestBody Galeria galeria) {
        return galeriaService.guardar(galeria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        galeriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}