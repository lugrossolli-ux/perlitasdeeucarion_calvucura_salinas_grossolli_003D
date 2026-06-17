package com.example.service_calendario.controller;

import com.example.service_calendario.model.Produccion;
import com.example.service_calendario.service.ProduccionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/produccion")
public class ProduccionController {

    private final ProduccionService produccionService;

    public ProduccionController(ProduccionService produccionService) {
        this.produccionService = produccionService;
    }

    @GetMapping
    public ResponseEntity<List<Produccion>> listarTodas() {
        return ResponseEntity.ok(produccionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produccion> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(produccionService.buscarPorId(id));
    }

    @GetMapping("/calendario")
    public ResponseEntity<List<Produccion>> calendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(produccionService.obtenerCalendario(desde, hasta));
    }

    @PostMapping
    public ResponseEntity<Produccion> crear(@RequestBody Produccion produccion) {
        Produccion creada = produccionService.crear(produccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produccion> actualizar(@PathVariable Integer id,
                                                  @RequestBody Produccion produccion) {
        return ResponseEntity.ok(produccionService.actualizar(id, produccion));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Produccion> cambiarEstado(@PathVariable Integer id,
                                                     @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(produccionService.cambiarEstado(id, body.get("estado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        produccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}