package com.example.reportes.controller;

import com.example.reportes.exception.ResourceNotFoundException;
import com.example.reportes.model.Reporte;
import com.example.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "Gestión de reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Listar todos los reportes")
    @GetMapping
    public List<Reporte> listarTodos() {
        return reporteService.listarTodos();
    }

    @Operation(summary = "Buscar un reporte por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@PathVariable Long id) {
        return reporteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + id));
    }

    @Operation(summary = "Crear un nuevo reporte")
    @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente")
    @PostMapping
    public ResponseEntity<Reporte> guardar(@Valid @RequestBody Reporte reporte) {
        return new ResponseEntity<>(reporteService.guardar(reporte), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un reporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reporte eliminado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Generar reporte de ventas por periodo")
    @GetMapping("/generar/ventas")
    public ResponseEntity<Reporte> generarReporteVentas(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().minusDays(30)}") LocalDate desde,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate hasta) {
        return ResponseEntity.ok(reporteService.generarReporteVentas(desde, hasta));
    }

    @Operation(summary = "Generar reporte de stock crítico")
    @GetMapping("/generar/stock-critico")
    public ResponseEntity<Reporte> generarReporteStockCritico() {
        return ResponseEntity.ok(reporteService.generarReporteStockCritico());
    }

    @Operation(summary = "Generar reporte de estado de pedidos")
    @GetMapping("/generar/pedidos")
    public ResponseEntity<Reporte> generarReportePedidos() {
        return ResponseEntity.ok(reporteService.generarReportePedidos());
    }
}