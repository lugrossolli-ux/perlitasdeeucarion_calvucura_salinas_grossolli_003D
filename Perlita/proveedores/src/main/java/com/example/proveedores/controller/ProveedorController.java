package com.example.proveedores.controller;
import com.example.proveedores.exception.ResourceNotFoundException;
import com.example.proveedores.model.Proveedor;
import com.example.proveedores.service.ProveedorService;
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
@RequestMapping("/proveedores")
@Tag(name = "Proveedores", description = "Gestión del directorio de proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService service;

    @Operation(summary = "Listar todos los proveedores")
    @GetMapping
    public List<Proveedor> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Listar proveedores activos")
    @GetMapping("/activos")
    public List<Proveedor> listarActivos() {
        return service.listarActivos();
    }

    @Operation(summary = "Buscar proveedor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
    }

    @Operation(summary = "Crear un nuevo proveedor")
    @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente")
    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody Proveedor proveedor) {
        return new ResponseEntity<>(service.guardar(proveedor), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un proveedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setId(id);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    @Operation(summary = "Eliminar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor eliminado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desactivar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor desactivado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Proveedor> desactivar(@PathVariable Long id) {
        Proveedor proveedor = service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setActivo(0);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    @Operation(summary = "Activar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor activado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Proveedor> activar(@PathVariable Long id) {
        Proveedor proveedor = service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setActivo(1);
        return ResponseEntity.ok(service.guardar(proveedor));
    }
}
