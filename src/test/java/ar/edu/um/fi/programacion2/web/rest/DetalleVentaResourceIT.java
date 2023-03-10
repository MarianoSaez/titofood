package ar.edu.um.fi.programacion2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.fi.programacion2.IntegrationTest;
import ar.edu.um.fi.programacion2.domain.DetalleVenta;
import ar.edu.um.fi.programacion2.repository.DetalleVentaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DetalleVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetalleVentaResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Float DEFAULT_SUBTOTAL = 1F;
    private static final Float UPDATED_SUBTOTAL = 2F;

    private static final Double DEFAULT_FOREIGN_ID = 1D;
    private static final Double UPDATED_FOREIGN_ID = 2D;

    private static final String ENTITY_API_URL = "/api/detalle-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetalleVentaMockMvc;

    private DetalleVenta detalleVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createEntity(EntityManager em) {
        DetalleVenta detalleVenta = new DetalleVenta().cantidad(DEFAULT_CANTIDAD).subtotal(DEFAULT_SUBTOTAL).foreignId(DEFAULT_FOREIGN_ID);
        return detalleVenta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createUpdatedEntity(EntityManager em) {
        DetalleVenta detalleVenta = new DetalleVenta().cantidad(UPDATED_CANTIDAD).subtotal(UPDATED_SUBTOTAL).foreignId(UPDATED_FOREIGN_ID);
        return detalleVenta;
    }

    @BeforeEach
    public void initTest() {
        detalleVenta = createEntity(em);
    }

    @Test
    @Transactional
    void createDetalleVenta() throws Exception {
        int databaseSizeBeforeCreate = detalleVentaRepository.findAll().size();
        // Create the DetalleVenta
        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleVenta)))
            .andExpect(status().isCreated());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeCreate + 1);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testDetalleVenta.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testDetalleVenta.getForeignId()).isEqualTo(DEFAULT_FOREIGN_ID);
    }

    @Test
    @Transactional
    void createDetalleVentaWithExistingId() throws Exception {
        // Create the DetalleVenta with an existing ID
        detalleVenta.setId(1L);

        int databaseSizeBeforeCreate = detalleVentaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleVenta)))
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetalleVentas() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].foreignId").value(hasItem(DEFAULT_FOREIGN_ID.doubleValue())));
    }

    @Test
    @Transactional
    void getDetalleVenta() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get the detalleVenta
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, detalleVenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalleVenta.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.doubleValue()))
            .andExpect(jsonPath("$.foreignId").value(DEFAULT_FOREIGN_ID.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDetalleVenta() throws Exception {
        // Get the detalleVenta
        restDetalleVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDetalleVenta() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();

        // Update the detalleVenta
        DetalleVenta updatedDetalleVenta = detalleVentaRepository.findById(detalleVenta.getId()).get();
        // Disconnect from session so that the updates on updatedDetalleVenta are not directly saved in db
        em.detach(updatedDetalleVenta);
        updatedDetalleVenta.cantidad(UPDATED_CANTIDAD).subtotal(UPDATED_SUBTOTAL).foreignId(UPDATED_FOREIGN_ID);

        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetalleVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testDetalleVenta.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testDetalleVenta.getForeignId()).isEqualTo(UPDATED_FOREIGN_ID);
    }

    @Test
    @Transactional
    void putNonExistingDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleVenta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testDetalleVenta.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testDetalleVenta.getForeignId()).isEqualTo(DEFAULT_FOREIGN_ID);
    }

    @Test
    @Transactional
    void fullUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        partialUpdatedDetalleVenta.cantidad(UPDATED_CANTIDAD).subtotal(UPDATED_SUBTOTAL).foreignId(UPDATED_FOREIGN_ID);

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testDetalleVenta.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testDetalleVenta.getForeignId()).isEqualTo(UPDATED_FOREIGN_ID);
    }

    @Test
    @Transactional
    void patchNonExistingDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetalleVenta() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeDelete = detalleVentaRepository.findAll().size();

        // Delete the detalleVenta
        restDetalleVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, detalleVenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
