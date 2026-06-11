package com.example.gastos.service;

import com.example.gastos.model.CategoriaGasto;
import com.example.gastos.repository.CategoriaGastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaGastoService {

    @Autowired
    private CategoriaGastoRepository repository;

    public List<CategoriaGasto> listarTodas() {
        return repository.findAll();
    }

    public Optional<CategoriaGasto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public CategoriaGasto guardar(CategoriaGasto categoria) {
        return repository.save(categoria);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}