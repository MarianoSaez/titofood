package ar.edu.um.fi.programacion2.service;

import ar.edu.um.fi.programacion2.domain.Menu;
import ar.edu.um.fi.programacion2.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Menu}.
 */
@Service
@Transactional
public class MenuService {

    private final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * Update all menus given a list of the active ones
     *
     * @param menus is a list a menu entities
     * @return the list of all active menus
     */
    public List<Menu> updateActiveMenus(List<Menu> menus) {
        // Se deben evaluar los casos donde:
        //  - Se haya agregado un nuevo menu
        //  - Se haya dado de baja/desactivado un menu
        //  - Se haya modificado atributos de un menu existente
        for (Menu menu : menus) {
            // Intentar obtener desde la DB el menu actual
            Optional<Menu> savedMenu = menuRepository.findOneByForeignId((double) menu.getId());
            if (savedMenu.isPresent()) {
                // Como este menu ya existe en la DB ahora controlamos si fue actualizado
            } else {
                // Como no existe en la DB y es un menu activo actualmente lo agregamos
                menuRepository.save(menu);
                log.info("New Menu entity saved : {}", menu);
            }
        }

        // Se debe agregar en la interfaz MenuRepository un metodo que retorne los menus activos
        // que seria una especie de findAllByIsActiveTrue o algo asi, hay que investigar
        // en la docu de los repositories
        return menuRepository.findAll(); // Provisorio para cumplir con el tipo de dato retornado
    }

    /**
     * Save a menu.
     *
     * @param menu the entity to save.
     * @return the persisted entity.
     */
    public Menu save(Menu menu) {
        log.debug("Request to save Menu : {}", menu);
        return menuRepository.save(menu);
    }

    /**
     * Update a menu.
     *
     * @param menu the entity to save.
     * @return the persisted entity.
     */
    public Menu update(Menu menu) {
        log.debug("Request to update Menu : {}", menu);
        return menuRepository.save(menu);
    }

    /**
     * Partially update a menu.
     *
     * @param menu the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Menu> partialUpdate(Menu menu) {
        log.debug("Request to partially update Menu : {}", menu);

        return menuRepository
            .findById(menu.getId())
            .map(existingMenu -> {
                if (menu.getNombre() != null) {
                    existingMenu.setNombre(menu.getNombre());
                }
                if (menu.getPrecio() != null) {
                    existingMenu.setPrecio(menu.getPrecio());
                }
                if (menu.getDescripcion() != null) {
                    existingMenu.setDescripcion(menu.getDescripcion());
                }
                if (menu.getUrlImagen() != null) {
                    existingMenu.setUrlImagen(menu.getUrlImagen());
                }
                if (menu.getIsActive() != null) {
                    existingMenu.setIsActive(menu.getIsActive());
                }
                if (menu.getForeignId() != null) {
                    existingMenu.setForeignId(menu.getForeignId());
                }
                if (menu.getCreado() != null) {
                    existingMenu.setCreado(menu.getCreado());
                }
                if (menu.getActualizado() != null) {
                    existingMenu.setActualizado(menu.getActualizado());
                }

                return existingMenu;
            })
            .map(menuRepository::save);
    }

    /**
     * Get all the menus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Menu> findAll(Pageable pageable) {
        log.debug("Request to get all Menus");
        return menuRepository.findAll(pageable);
    }

    /**
     * Get one menu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Menu> findOne(Long id) {
        log.debug("Request to get Menu : {}", id);
        return menuRepository.findById(id);
    }

    /**
     * Delete the menu by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Menu : {}", id);
        menuRepository.deleteById(id);
    }
}
