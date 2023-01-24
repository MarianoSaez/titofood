package ar.edu.um.fi.programacion2.repository;

import ar.edu.um.fi.programacion2.domain.Menu;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MenuRepositoryWithBagRelationshipsImpl implements MenuRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Menu> fetchBagRelationships(Optional<Menu> menu) {
        return menu.map(this::fetchVentas);
    }

    @Override
    public Page<Menu> fetchBagRelationships(Page<Menu> menus) {
        return new PageImpl<>(fetchBagRelationships(menus.getContent()), menus.getPageable(), menus.getTotalElements());
    }

    @Override
    public List<Menu> fetchBagRelationships(List<Menu> menus) {
        return Optional.of(menus).map(this::fetchVentas).orElse(Collections.emptyList());
    }

    Menu fetchVentas(Menu result) {
        return entityManager
            .createQuery("select menu from Menu menu left join fetch menu.ventas where menu is :menu", Menu.class)
            .setParameter("menu", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Menu> fetchVentas(List<Menu> menus) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, menus.size()).forEach(index -> order.put(menus.get(index).getId(), index));
        List<Menu> result = entityManager
            .createQuery("select distinct menu from Menu menu left join fetch menu.ventas where menu in :menus", Menu.class)
            .setParameter("menus", menus)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
