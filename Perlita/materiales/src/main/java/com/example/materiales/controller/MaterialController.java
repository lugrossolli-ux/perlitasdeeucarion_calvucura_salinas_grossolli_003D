package com.example.materiales.controller;

import com.example.materiales.exception.ResourceNotFoundException;
import com.example.materiales.model.Material;
import com.example.materiales.service.MaterialService;
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
@RequestMapping("/materiales")
@Tag(name = "Materiales", description = "Gestión de materiales e inventario")
public class MaterialController {

    @Autowired
    private MaterialService service;

    @Operation(summary = "Listar todos los materiales")
    @GetMapping
    public List<Material> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Buscar material por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material encontrado"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Material> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
    }

    @Operation(summary = "Crear un material")
    @ApiResponse(responseCode = "201", description = "Material creado exitosamente")
    @PostMapping
    public ResponseEntity<Material> crear(@Valid @RequestBody Material material) {
        return new ResponseEntity<>(service.guardar(material), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material actualizado"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Material> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody Material material) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
        material.setId(id);
        return ResponseEntity.ok(service.guardar(material));
    }

    @Operation(summary = "Eliminar un material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material eliminado"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar materiales con stock bajo",
               description = "Retorna materiales cuyo stock actual está por debajo del mínimo")
    @GetMapping("/stock-bajo")
    public List<Material> stockBajo() {
        return service.stockBajo();
    }
}
