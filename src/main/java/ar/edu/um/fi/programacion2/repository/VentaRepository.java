package ar.edu.um.fi.programacion2.repository;

import ar.edu.um.fi.programacion2.domain.Venta;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findAllByFechaBetween(Instant fechaInicio, Instant fechaFin);
}
