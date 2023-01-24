package ar.edu.um.fi.programacion2.repository;

import ar.edu.um.fi.programacion2.domain.Menu;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Menu entity.
 *
 * When extending this class, extend MenuRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MenuRepository extends MenuRepositoryWithBagRelationships, JpaRepository<Menu, Long> {
    default Optional<Menu> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Menu> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Menu> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    Optional<Menu> findOneByForeignId(Double foreign_id);

    List<Menu> findByForeignIdIsNullOrForeignIdNotIn(Collection<Double> foreignIds);

    List<Menu> findByIsActiveTrue();
}
