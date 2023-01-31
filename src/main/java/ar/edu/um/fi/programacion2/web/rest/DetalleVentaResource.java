package ar.edu.um.fi.programacion2.web.rest;

import ar.edu.um.fi.programacion2.domain.DetalleVenta;
import ar.edu.um.fi.programacion2.repository.DetalleVentaRepository;
import ar.edu.um.fi.programacion2.service.DetalleVentaService;
import ar.edu.um.fi.programacion2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.fi.programacion2.domain.DetalleVenta}.
 */
@RestController
@RequestMapping("/api")
public class DetalleVentaResource {

    private final Logger log = LoggerFactory.getLogger(DetalleVentaResource.class);

    private static final String ENTITY_NAME = "detalleVenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleVentaService detalleVentaService;

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaResource(DetalleVentaService detalleVentaService, DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaService = detalleVentaService;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    /**
     * {@code POST  /detalle-ventas} : Create a new detalleVenta.
     *
     * @param detalleVenta the detalleVenta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleVenta, or with status {@code 400 (Bad Request)} if the detalleVenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detalle-ventas")
    public ResponseEntity<DetalleVenta> createDetalleVenta(@RequestBody DetalleVenta detalleVenta) throws URISyntaxException {
        log.debug("REST request to save DetalleVenta : {}", detalleVenta);
        if (detalleVenta.getId() != null) {
            throw new BadRequestAlertException("A new detalleVenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetalleVenta result = detalleVentaService.save(detalleVenta);
        return ResponseEntity
            .created(new URI("/api/detalle-ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detalle-ventas/:id} : Updates an existing detalleVenta.
     *
     * @param id the id of the detalleVenta to save.
     * @param detalleVenta the detalleVenta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleVenta,
     * or with status {@code 400 (Bad Request)} if the detalleVenta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleVenta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detalle-ventas/{id}")
    public ResponseEntity<DetalleVenta> updateDetalleVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetalleVenta detalleVenta
    ) throws URISyntaxException {
        log.debug("REST request to update DetalleVenta : {}, {}", id, detalleVenta);
        if (detalleVenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleVenta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetalleVenta result = detalleVentaService.update(detalleVenta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detalleVenta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detalle-ventas/:id} : Partial updates given fields of an existing detalleVenta, field will ignore if it is null
     *
     * @param id the id of the detalleVenta to save.
     * @param detalleVenta the detalleVenta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleVenta,
     * or with status {@code 400 (Bad Request)} if the detalleVenta is not valid,
     * or with status {@code 404 (Not Found)} if the detalleVenta is not found,
     * or with status {@code 500 (Internal Server Error)} if the detalleVenta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detalle-ventas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetalleVenta> partialUpdateDetalleVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetalleVenta detalleVenta
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetalleVenta partially : {}, {}", id, detalleVenta);
        if (detalleVenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleVenta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetalleVenta> result = detalleVentaService.partialUpdate(detalleVenta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detalleVenta.getId().toString())
        );
    }

    /**
     * {@code GET  /detalle-ventas} : get all the detalleVentas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalleVentas in body.
     */
    @GetMapping("/detalle-ventas")
    public List<DetalleVenta> getAllDetalleVentas() {
        log.debug("REST request to get all DetalleVentas");
        return detalleVentaService.findAll();
    }

    /**
     * {@code GET  /detalle-ventas/:id} : get the "id" detalleVenta.
     *
     * @param id the id of the detalleVenta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleVenta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detalle-ventas/{id}")
    public ResponseEntity<DetalleVenta> getDetalleVenta(@PathVariable Long id) {
        log.debug("REST request to get DetalleVenta : {}", id);
        Optional<DetalleVenta> detalleVenta = detalleVentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detalleVenta);
    }

    /**
     * {@code DELETE  /detalle-ventas/:id} : delete the "id" detalleVenta.
     *
     * @param id the id of the detalleVenta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detalle-ventas/{id}")
    public ResponseEntity<Void> deleteDetalleVenta(@PathVariable Long id) {
        log.debug("REST request to delete DetalleVenta : {}", id);
        detalleVentaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
