package com.example.proveedores.controller;


import com.example.proveedores.model.ProveedorMaterial;
import com.example.proveedores.service.ProveedorMaterialService;
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
@RequestMapping("/proveedor-materiales")
@Tag(name = "Materiales del Proveedor", description = "Gestión de materiales ofrecidos por proveedores")
public class ProveedorMaterialController {

    @Autowired
    private ProveedorMaterialService service;

    @Operation(summary = "Listar materiales de un proveedor")
    @GetMapping("/proveedor/{proveedorId}")
    public List<ProveedorMaterial> listarPorProveedor(@PathVariable Long proveedorId) {
        return service.listarPorProveedor(proveedorId);
    }

    @Operation(summary = "Listar proveedores de un material")
    @GetMapping("/material/{materialId}")
    public List<ProveedorMaterial> listarPorMaterial(@PathVariable Long materialId) {
        return service.listarPorMaterial(materialId);
    }

    @Operation(summary = "Asociar material a un proveedor")
    @ApiResponse(responseCode = "201", description = "Material asociado exitosamente")
    @PostMapping
    public ResponseEntity<ProveedorMaterial> crear(@Valid @RequestBody ProveedorMaterial proveedorMaterial) {
        return new ResponseEntity<>(service.guardar(proveedorMaterial), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar precio de material de proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precio actualizado"),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorMaterial> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody ProveedorMaterial proveedorMaterial) {
        if (!service.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        proveedorMaterial.setId(id);
        return ResponseEntity.ok(service.guardar(proveedorMaterial));
    }

    @Operation(summary = "Eliminar material de proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relación eliminada"),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!service.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
