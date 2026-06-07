package com.example.provedores.controller;
import com.example.provedores.exception.ResourceNotFoundException;
import com.example.provedores.model.Proveedor;
import com.example.provedores.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService service;

    @GetMapping
    public List<Proveedor> listar() {
        return service.listarTodos();
    }

    @GetMapping("/activos")
    public List<Proveedor> listarActivos() {
        return service.listarActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return new ResponseEntity<>(service.guardar(proveedor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setId(id);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Proveedor> desactivar(@PathVariable Long id) {
        Proveedor proveedor = service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setActivo(0);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Proveedor> activar(@PathVariable Long id) {
        Proveedor proveedor = service.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setActivo(1);
        return ResponseEntity.ok(service.guardar(proveedor));
    }
}