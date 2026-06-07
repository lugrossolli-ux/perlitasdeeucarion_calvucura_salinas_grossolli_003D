package com.example.provedores.service;

import com.example.provedores.model.Proveedor;
import com.example.provedores.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository repository;

    public List<Proveedor> listarTodos() {
        return repository.findAll();
    }

    public List<Proveedor> listarActivos() {
        return repository.findByActivo(1);
    }

    public Optional<Proveedor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return repository.save(proveedor);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}