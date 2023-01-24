package ar.edu.um.fi.programacion2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.fi.programacion2.IntegrationTest;
import ar.edu.um.fi.programacion2.domain.Menu;
import ar.edu.um.fi.programacion2.repository.MenuRepository;
import ar.edu.um.fi.programacion2.service.MenuService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MenuResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MenuResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_URL_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_URL_IMAGEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Double DEFAULT_FOREIGN_ID = 1D;
    private static final Double UPDATED_FOREIGN_ID = 2D;

    private static final String DEFAULT_CREADO = "AAAAAAAAAA";
    private static final String UPDATED_CREADO = "BBBBBBBBBB";

    private static final String DEFAULT_ACTUALIZADO = "AAAAAAAAAA";
    private static final String UPDATED_ACTUALIZADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuRepository menuRepository;

    @Mock
    private MenuRepository menuRepositoryMock;

    @Mock
    private MenuService menuServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuMockMvc;

    private Menu menu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createEntity(EntityManager em) {
        Menu menu = new Menu()
            .nombre(DEFAULT_NOMBRE)
            .precio(DEFAULT_PRECIO)
            .descripcion(DEFAULT_DESCRIPCION)
            .urlImagen(DEFAULT_URL_IMAGEN)
            .isActive(DEFAULT_IS_ACTIVE)
            .foreignId(DEFAULT_FOREIGN_ID)
            .creado(DEFAULT_CREADO)
            .actualizado(DEFAULT_ACTUALIZADO);
        return menu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createUpdatedEntity(EntityManager em) {
        Menu menu = new Menu()
            .nombre(UPDATED_NOMBRE)
            .precio(UPDATED_PRECIO)
            .descripcion(UPDATED_DESCRIPCION)
            .urlImagen(UPDATED_URL_IMAGEN)
            .isActive(UPDATED_IS_ACTIVE)
            .foreignId(UPDATED_FOREIGN_ID)
            .creado(UPDATED_CREADO)
            .actualizado(UPDATED_ACTUALIZADO);
        return menu;
    }

    @BeforeEach
    public void initTest() {
        menu = createEntity(em);
    }

    @Test
    @Transactional
    void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().size();
        // Create the Menu
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isCreated());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate + 1);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMenu.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testMenu.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMenu.getUrlImagen()).isEqualTo(DEFAULT_URL_IMAGEN);
        assertThat(testMenu.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testMenu.getForeignId()).isEqualTo(DEFAULT_FOREIGN_ID);
        assertThat(testMenu.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testMenu.getActualizado()).isEqualTo(DEFAULT_ACTUALIZADO);
    }

    @Test
    @Transactional
    void createMenuWithExistingId() throws Exception {
        // Create the Menu with an existing ID
        menu.setId(1L);

        int databaseSizeBeforeCreate = menuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMenus() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].urlImagen").value(hasItem(DEFAULT_URL_IMAGEN)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].foreignId").value(hasItem(DEFAULT_FOREIGN_ID.doubleValue())))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO)))
            .andExpect(jsonPath("$.[*].actualizado").value(hasItem(DEFAULT_ACTUALIZADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenusWithEagerRelationshipsIsEnabled() throws Exception {
        when(menuServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(menuServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenusWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(menuServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(menuRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get the menu
        restMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menu.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.urlImagen").value(DEFAULT_URL_IMAGEN))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.foreignId").value(DEFAULT_FOREIGN_ID.doubleValue()))
            .andExpect(jsonPath("$.creado").value(DEFAULT_CREADO))
            .andExpect(jsonPath("$.actualizado").value(DEFAULT_ACTUALIZADO));
    }

    @Test
    @Transactional
    void getNonExistingMenu() throws Exception {
        // Get the menu
        restMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu
        Menu updatedMenu = menuRepository.findById(menu.getId()).get();
        // Disconnect from session so that the updates on updatedMenu are not directly saved in db
        em.detach(updatedMenu);
        updatedMenu
            .nombre(UPDATED_NOMBRE)
            .precio(UPDATED_PRECIO)
            .descripcion(UPDATED_DESCRIPCION)
            .urlImagen(UPDATED_URL_IMAGEN)
            .isActive(UPDATED_IS_ACTIVE)
            .foreignId(UPDATED_FOREIGN_ID)
            .creado(UPDATED_CREADO)
            .actualizado(UPDATED_ACTUALIZADO);

        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMenu.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testMenu.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMenu.getUrlImagen()).isEqualTo(UPDATED_URL_IMAGEN);
        assertThat(testMenu.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testMenu.getForeignId()).isEqualTo(UPDATED_FOREIGN_ID);
        assertThat(testMenu.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testMenu.getActualizado()).isEqualTo(UPDATED_ACTUALIZADO);
    }

    @Test
    @Transactional
    void putNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu.urlImagen(UPDATED_URL_IMAGEN);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMenu.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testMenu.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMenu.getUrlImagen()).isEqualTo(UPDATED_URL_IMAGEN);
        assertThat(testMenu.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testMenu.getForeignId()).isEqualTo(DEFAULT_FOREIGN_ID);
        assertThat(testMenu.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testMenu.getActualizado()).isEqualTo(DEFAULT_ACTUALIZADO);
    }

    @Test
    @Transactional
    void fullUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .nombre(UPDATED_NOMBRE)
            .precio(UPDATED_PRECIO)
            .descripcion(UPDATED_DESCRIPCION)
            .urlImagen(UPDATED_URL_IMAGEN)
            .isActive(UPDATED_IS_ACTIVE)
            .foreignId(UPDATED_FOREIGN_ID)
            .creado(UPDATED_CREADO)
            .actualizado(UPDATED_ACTUALIZADO);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMenu.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testMenu.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMenu.getUrlImagen()).isEqualTo(UPDATED_URL_IMAGEN);
        assertThat(testMenu.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testMenu.getForeignId()).isEqualTo(UPDATED_FOREIGN_ID);
        assertThat(testMenu.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testMenu.getActualizado()).isEqualTo(UPDATED_ACTUALIZADO);
    }

    @Test
    @Transactional
    void patchNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeDelete = menuRepository.findAll().size();

        // Delete the menu
        restMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, menu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
