package com.example.provedores.repository;
import com.example.provedores.model.ProveedorMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProveedorMaterialRepository extends JpaRepository<ProveedorMaterial, Long> {
    List<ProveedorMaterial> findByProveedorId(Long proveedorId);
    List<ProveedorMaterial> findByMaterialId(Long materialId);
}