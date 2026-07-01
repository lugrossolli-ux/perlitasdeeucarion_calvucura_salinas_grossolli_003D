package com.example.service_calendario.controller;

import com.example.service_calendario.model.Bloqueo;
import com.example.service_calendario.service.BloqueoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/produccion/bloqueos")
@Tag(name = "Bloqueos", description = "Gestión de bloqueos del calendario de producción")
public class BloqueoController {

    private final BloqueoService bloqueoService;

    public BloqueoController(BloqueoService bloqueoService) {
        this.bloqueoService = bloqueoService;
    }

    @Operation(summary = "Listar todos los bloqueos")
    @GetMapping
    public ResponseEntity<List<Bloqueo>> listarTodos() {
        return ResponseEntity.ok(bloqueoService.listarTodos());
    }

    @Operation(summary = "Crear un bloqueo", description = "Impide crear producciones en el rango de fechas especificado")
    @ApiResponse(responseCode = "201", description = "Bloqueo creado exitosamente")
    @PostMapping
    public ResponseEntity<Bloqueo> crear(@Valid @RequestBody Bloqueo bloqueo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bloqueoService.crear(bloqueo));
    }

    @Operation(summary = "Eliminar un bloqueo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bloqueo eliminado"),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        bloqueoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
