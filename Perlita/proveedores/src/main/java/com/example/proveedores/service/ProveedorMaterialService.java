package com.example.proveedores.service;

import com.example.proveedores.model.ProveedorMaterial;
import com.example.proveedores.repository.ProveedorMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorMaterialService {

    @Autowired
    private ProveedorMaterialRepository repository;

    public List<ProveedorMaterial> listarPorProveedor(Long proveedorId) {
        return repository.findByProveedorId(proveedorId);
    }

    public List<ProveedorMaterial> listarPorMaterial(Long materialId) {
        return repository.findByMaterialId(materialId);
    }

    public Optional<ProveedorMaterial> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public ProveedorMaterial guardar(ProveedorMaterial proveedorMaterial) {
        proveedorMaterial.setFechaActualizacion(LocalDate.now());
        return repository.save(proveedorMaterial);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }
}