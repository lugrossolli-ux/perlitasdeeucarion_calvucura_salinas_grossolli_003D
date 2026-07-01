package com.example.materiales.service;

import com.example.materiales.model.Material;
import com.example.materiales.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    public List<Material> listarTodos() {
        return repository.findAll();
    }

    public Optional<Material> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Material guardar(Material material) {
        material.setFechaActualizacion(LocalDate.now());
        return repository.save(material);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Material> stockBajo() {
        return repository.findStockBajo();
    }
}