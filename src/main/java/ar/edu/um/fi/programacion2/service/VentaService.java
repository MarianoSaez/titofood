package ar.edu.um.fi.programacion2.service;

import ar.edu.um.fi.programacion2.domain.Venta;
import ar.edu.um.fi.programacion2.repository.VentaRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<Venta> findBetweenDate(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Venta> ventas = ventaRepository.findAllByFechaBetween(
            fechaInicio.toInstant(ZoneOffset.UTC),
            fechaFin.toInstant(ZoneOffset.UTC)
        );
        return ventas;
    }

    /**
     * Save a venta.
     *
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    public Venta save(Venta venta) {
        log.debug("Request to save Venta : {}", venta);
        return ventaRepository.save(venta);
    }

    /**
     * Update a venta.
     *
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    public Venta update(Venta venta) {
        log.debug("Request to update Venta : {}", venta);
        return ventaRepository.save(venta);
    }

    /**
     * Partially update a venta.
     *
     * @param venta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Venta> partialUpdate(Venta venta) {
        log.debug("Request to partially update Venta : {}", venta);

        return ventaRepository
            .findById(venta.getId())
            .map(existingVenta -> {
                if (venta.getFecha() != null) {
                    existingVenta.setFecha(venta.getFecha());
                }
                if (venta.getPrecio() != null) {
                    existingVenta.setPrecio(venta.getPrecio());
                }
                if (venta.getCodigoSeguimiento() != null) {
                    existingVenta.setCodigoSeguimiento(venta.getCodigoSeguimiento());
                }

                return existingVenta;
            })
            .map(ventaRepository::save);
    }

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Venta> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        return ventaRepository.findAll(pageable);
    }

    /**
     * Get one venta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Venta> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findById(id);
    }

    /**
     * Delete the venta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
