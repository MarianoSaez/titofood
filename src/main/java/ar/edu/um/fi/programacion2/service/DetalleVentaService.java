package ar.edu.um.fi.programacion2.service;

import ar.edu.um.fi.programacion2.domain.DetalleVenta;
import ar.edu.um.fi.programacion2.repository.DetalleVentaRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DetalleVenta}.
 */
@Service
@Transactional
public class DetalleVentaService {

    private final Logger log = LoggerFactory.getLogger(DetalleVentaService.class);

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    /**
     * Save a detalleVenta.
     *
     * @param detalleVenta the entity to save.
     * @return the persisted entity.
     */
    public DetalleVenta save(DetalleVenta detalleVenta) {
        log.debug("Request to save DetalleVenta : {}", detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    /**
     * Update a detalleVenta.
     *
     * @param detalleVenta the entity to save.
     * @return the persisted entity.
     */
    public DetalleVenta update(DetalleVenta detalleVenta) {
        log.debug("Request to update DetalleVenta : {}", detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    /**
     * Partially update a detalleVenta.
     *
     * @param detalleVenta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DetalleVenta> partialUpdate(DetalleVenta detalleVenta) {
        log.debug("Request to partially update DetalleVenta : {}", detalleVenta);

        return detalleVentaRepository
            .findById(detalleVenta.getId())
            .map(existingDetalleVenta -> {
                if (detalleVenta.getCantidad() != null) {
                    existingDetalleVenta.setCantidad(detalleVenta.getCantidad());
                }
                if (detalleVenta.getSubtotal() != null) {
                    existingDetalleVenta.setSubtotal(detalleVenta.getSubtotal());
                }

                return existingDetalleVenta;
            })
            .map(detalleVentaRepository::save);
    }

    /**
     * Get all the detalleVentas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DetalleVenta> findAll() {
        log.debug("Request to get all DetalleVentas");
        return detalleVentaRepository.findAll();
    }

    /**
     * Get one detalleVenta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> findOne(Long id) {
        log.debug("Request to get DetalleVenta : {}", id);
        return detalleVentaRepository.findById(id);
    }

    /**
     * Delete the detalleVenta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DetalleVenta : {}", id);
        detalleVentaRepository.deleteById(id);
    }
}
