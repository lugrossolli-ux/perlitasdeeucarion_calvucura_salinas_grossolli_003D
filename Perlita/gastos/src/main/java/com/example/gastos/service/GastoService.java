package com.example.gastos.service;

import com.example.gastos.model.Gasto;
import com.example.gastos.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GastoService {
    @Autowired
    private GastoRepository repository;

    public List<Gasto> listarTodos() {
        return repository.findAll();
    }

    public List<Gasto> listarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    public List<Gasto> listarPorProveedor(Long proveedorId) {
        return repository.findByProveedorId(proveedorId);
    }

    public Optional<Gasto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Gasto guardar(Gasto gasto) {
        if (gasto.getFecha() == null) {
            gasto.setFecha(LocalDate.now());
        }
        return repository.save(gasto);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}