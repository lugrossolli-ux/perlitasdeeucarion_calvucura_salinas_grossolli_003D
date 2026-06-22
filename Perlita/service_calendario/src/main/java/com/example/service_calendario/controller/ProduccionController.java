package com.example.service_calendario.controller;

import com.example.service_calendario.model.Produccion;
import com.example.service_calendario.service.ProduccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/produccion")
@Tag(name = "Producción", description = "Gestión de órdenes de producción")
public class ProduccionController {

    private final ProduccionService produccionService;

    public ProduccionController(ProduccionService produccionService) {
        this.produccionService = produccionService;
    }

    @Operation(summary = "Listar todas las producciones",
               description = "Retorna todas las órdenes enriquecidas con datos de producto y pedido")
    @GetMapping
    public ResponseEntity<List<Produccion>> listarTodas() {
        return ResponseEntity.ok(produccionService.listarTodas());
    }

    @Operation(summary = "Buscar producción por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producción encontrada"),
            @ApiResponse(responseCode = "404", description = "Producción no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produccion> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(produccionService.buscarPorId(id));
    }

    @Operation(summary = "Filtrar por rango de fechas",
               description = "Retorna producciones cuyo rango solapa con las fechas indicadas")
    @GetMapping("/calendario")
    public ResponseEntity<List<Produccion>> calendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(produccionService.obtenerCalendario(desde, hasta));
    }

    @Operation(summary = "Crear una producción",
               description = "Valida que no haya solapamiento con bloqueos existentes")
    @ApiResponse(responseCode = "201", description = "Producción creada exitosamente")
    @PostMapping
    public ResponseEntity<Produccion> crear(@Valid @RequestBody Produccion produccion) {
        Produccion creada = produccionService.crear(produccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Actualizar una producción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producción actualizada"),
            @ApiResponse(responseCode = "404", description = "Producción no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Produccion> actualizar(@PathVariable Integer id,
                                                  @Valid @RequestBody Produccion produccion) {
        return ResponseEntity.ok(produccionService.actualizar(id, produccion));
    }

    @Operation(summary = "Cambiar estado de producción",
               description = "Estados válidos: programado, en_proceso, finalizado, completado")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Produccion> cambiarEstado(@PathVariable Integer id,
                                                     @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(produccionService.cambiarEstado(id, body.get("estado")));
    }

    @Operation(summary = "Eliminar una producción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producción eliminada"),
            @ApiResponse(responseCode = "404", description = "Producción no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        produccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
