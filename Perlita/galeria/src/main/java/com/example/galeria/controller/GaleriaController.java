package com.example.galeria.controller;

import com.example.galeria.model.Galeria;
import com.example.galeria.service.GaleriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/galeria")
@CrossOrigin(origins = "*")
public class GaleriaController {

    @Autowired
    private GaleriaService galeriaService;

    @GetMapping
    public List<Galeria> listarTodos() {
        return galeriaService.listarTodos();
    }

    @Operation(summary = "Buscar una imagen de galería por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen encontrada"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Galeria> buscarPorId(@PathVariable Long id) {
        return galeriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Imagen de galería no encontrada con id: " + id));
    }

    @GetMapping("/producto/{productoId}")
    public List<Galeria> buscarPorProducto(@PathVariable Long productoId) {
        return galeriaService.buscarPorProducto(productoId);
    }

    @PostMapping
    public Galeria guardar(@Valid @RequestBody Galeria galeria) {
        return galeriaService.guardar(galeria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        galeriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}