package com.example.service_calendario.controller;

import com.example.service_calendario.model.Bloqueo;
import com.example.service_calendario.service.BloqueoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bloqueos")
public class BloqueoController {

    private final BloqueoService bloqueoService;

    public BloqueoController(BloqueoService bloqueoService) {
        this.bloqueoService = bloqueoService;
    }

    @GetMapping
    public ResponseEntity<List<Bloqueo>> listar() {
        return ResponseEntity.ok(bloqueoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Bloqueo> crear(@RequestBody Bloqueo bloqueo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bloqueoService.crear(bloqueo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        bloqueoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
