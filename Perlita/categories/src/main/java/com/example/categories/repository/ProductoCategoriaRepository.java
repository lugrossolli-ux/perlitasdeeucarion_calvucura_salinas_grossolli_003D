package com.example.categories.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.categories.model.ProductoCategoria;

@Repository

public interface ProductoCategoriaRepository extends JpaRepository<ProductoCategoria, Long>
{
    List<ProductoCategoria> findByCategoria_Id(Long categoriaId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductoCategoria pc SET pc.categoria.id = :destino WHERE pc.categoria.id = :origen")
    int reasignarProductos(@Param("origen") Long origen, @Param("destino") Long destino);
}
