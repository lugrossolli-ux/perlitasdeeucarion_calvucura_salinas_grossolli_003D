package com.example.galeria.service;

import com.example.galeria.model.Galeria;
import com.example.galeria.repository.GaleriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GaleriaService {

    @Autowired
    private GaleriaRepository galeriaRepository;

    public List<Galeria> listarTodos() {
        return galeriaRepository.findAll();
    }

    public Optional<Galeria> buscarPorId(Long id) {
        return galeriaRepository.findById(id);
    }

    public List<Galeria> buscarPorProducto(Long productoId) {
        return galeriaRepository.findByProductoId(productoId);
    }

    public Galeria guardar(Galeria galeria) {
        return galeriaRepository.save(galeria);
    }

    public void eliminar(Long id) {
        galeriaRepository.deleteById(id);
    }
}