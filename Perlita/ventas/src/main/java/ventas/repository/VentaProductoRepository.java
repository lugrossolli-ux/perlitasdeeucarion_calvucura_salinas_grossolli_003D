package ventas.repository;

import ventas.model.VentaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaProductoRepository extends JpaRepository<VentaProducto, Long> {
    List<VentaProducto> findByVentaId(Long ventaId);
}