package com.example.gastos.controller;

import com.example.gastos.exception.ResourceNotFoundException;
import com.example.gastos.model.Gasto;
import com.example.gastos.service.GastoService;
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
@RequestMapping("/gastos")
@Tag(name = "Gastos", description = "Gestión de gastos del negocio")
public class GastoController {

    @Autowired
    private GastoService service;

    @Operation(summary = "Listar todos los gastos")
    @GetMapping
    public List<Gasto> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Buscar gasto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gasto encontrado"),
            @ApiResponse(responseCode = "404", description = "Gasto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Gasto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id: " + id));
    }

    @Operation(summary = "Listar gastos por categoría")
    @GetMapping("/categoria/{categoriaId}")
    public List<Gasto> listarPorCategoria(@PathVariable Long categoriaId) {
        return service.listarPorCategoria(categoriaId);
    }

    @Operation(summary = "Listar gastos por proveedor")
    @GetMapping("/proveedor/{proveedorId}")
    public List<Gasto> listarPorProveedor(@PathVariable Long proveedorId) {
        return service.listarPorProveedor(proveedorId);
    }

    @Operation(summary = "Crear un nuevo gasto")
    @ApiResponse(responseCode = "201", description = "Gasto creado exitosamente")
    @PostMapping
    public ResponseEntity<Gasto> crear(@Valid @RequestBody Gasto gasto) {
        return new ResponseEntity<>(service.guardar(gasto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un gasto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gasto actualizado"),
            @ApiResponse(responseCode = "404", description = "Gasto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Gasto> actualizar(@PathVariable Long id,
                                             @Valid @RequestBody Gasto gasto) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id: " + id));
        gasto.setId(id);
        return ResponseEntity.ok(service.guardar(gasto));
    }

    @Operation(summary = "Eliminar un gasto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Gasto eliminado"),
            @ApiResponse(responseCode = "404", description = "Gasto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
