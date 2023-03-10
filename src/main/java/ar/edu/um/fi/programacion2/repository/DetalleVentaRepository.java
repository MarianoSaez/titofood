package ar.edu.um.fi.programacion2.repository;

import ar.edu.um.fi.programacion2.domain.DetalleVenta;
import ar.edu.um.fi.programacion2.domain.Venta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetalleVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVenta(Venta venta);
}
