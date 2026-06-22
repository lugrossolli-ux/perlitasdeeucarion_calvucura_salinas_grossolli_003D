package com.example.galeria.controller;

import com.example.galeria.exception.ResourceNotFoundException;
import com.example.galeria.model.Galeria;
import com.example.galeria.service.GaleriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/galeria")
@Tag(name = "Galería", description = "Gestión del portafolio visual de productos")
public class GaleriaController {

    @Autowired
    private GaleriaService galeriaService;

    @Operation(summary = "Listar todas las imágenes de la galería")
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
                .orElseThrow(() -> new ResourceNotFoundException("Imagen de galería no encontrada con id: " + id));
    }

    @Operation(summary = "Buscar imágenes por producto")
    @GetMapping("/producto/{productoId}")
    public List<Galeria> buscarPorProducto(@PathVariable Long productoId) {
        return galeriaService.buscarPorProducto(productoId);
    }

    @Operation(summary = "Agregar una nueva imagen a la galería")
    @ApiResponse(responseCode = "201", description = "Imagen agregada exitosamente")
    @PostMapping
    public ResponseEntity<Galeria> guardar(@Valid @RequestBody Galeria galeria) {
        return new ResponseEntity<>(galeriaService.guardar(galeria), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar una imagen de la galería")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagen eliminada"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        galeriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
