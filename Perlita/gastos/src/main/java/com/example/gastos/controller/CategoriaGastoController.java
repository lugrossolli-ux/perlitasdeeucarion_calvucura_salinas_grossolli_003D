package com.example.gastos.controller;

import com.example.gastos.exception.ResourceNotFoundException;
import com.example.gastos.model.CategoriaGasto;
import com.example.gastos.service.CategoriaGastoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/categorias-gasto")
@Tag(name = "Categorías de Gasto", description = "Gestión de categorías para clasificar gastos")
public class CategoriaGastoController {

    @Autowired
    private CategoriaGastoService service;

    @Operation(summary = "Listar todas las categorías de gasto")
    @GetMapping
    public List<CategoriaGasto> listar() {
        return service.listarTodas();
    }

    @Operation(summary = "Buscar categoría de gasto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaGasto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de gasto no encontrada con id: " + id));
    }

    @Operation(summary = "Crear una nueva categoría de gasto")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    @PostMapping
    public ResponseEntity<CategoriaGasto> crear(@Valid @RequestBody CategoriaGasto categoria) {
        return new ResponseEntity<>(service.guardar(categoria), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una categoría de gasto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaGasto> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody CategoriaGasto categoria) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de gasto no encontrada con id: " + id));
        categoria.setId(id);
        return ResponseEntity.ok(service.guardar(categoria));
    }

    @Operation(summary = "Eliminar una categoría de gasto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de gasto no encontrada con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
