package com.example.categories.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.categories.model.Categoria;
import com.example.categories.model.ProductoCategoria;
import com.example.categories.repository.CategoriaRepository;
import com.example.categories.repository.ProductoCategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoCategoriaRepository productoCategoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarActivas(Integer activo) {
        return categoriaRepository.findByActivo(activo);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    public List<ProductoCategoria> obtenerProductosDe(Long categoriaId) {
        return productoCategoriaRepository.findByCategoria_Id(categoriaId);
    }
}



