package com.example.provedores.controller;


import com.example.provedores.model.ProveedorMaterial;
import com.example.provedores.service.ProveedorMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/proveedor-materiales")
public class ProveedorMaterialController {

    @Autowired
    private ProveedorMaterialService service;

    @GetMapping("/proveedor/{proveedorId}")
    public List<ProveedorMaterial> listarPorProveedor(@PathVariable Long proveedorId) {
        return service.listarPorProveedor(proveedorId);
    }

    @GetMapping("/material/{materialId}")
    public List<ProveedorMaterial> listarPorMaterial(@PathVariable Long materialId) {
        return service.listarPorMaterial(materialId);
    }

    @PostMapping
    public ResponseEntity<ProveedorMaterial> crear(@RequestBody ProveedorMaterial proveedorMaterial) {
        return new ResponseEntity<>(service.guardar(proveedorMaterial), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorMaterial> actualizar(@PathVariable Long id,
                                                         @RequestBody ProveedorMaterial proveedorMaterial) {
        if (!service.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        proveedorMaterial.setId(id);
        return ResponseEntity.ok(service.guardar(proveedorMaterial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!service.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}