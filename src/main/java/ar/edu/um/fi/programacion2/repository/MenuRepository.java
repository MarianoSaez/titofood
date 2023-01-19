package ar.edu.um.fi.programacion2.repository;

import ar.edu.um.fi.programacion2.domain.Menu;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Menu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findOneByForeignId(Double foreign_id);

    List<Menu> findByForeignIdIsNullOrForeignIdNotIn(Collection<Double> foreignIds);

    List<Menu> findByIsActiveTrue();
}
