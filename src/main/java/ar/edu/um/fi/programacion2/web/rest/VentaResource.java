package ar.edu.um.fi.programacion2.web.rest;

import ar.edu.um.fi.programacion2.domain.DetalleVenta;
import ar.edu.um.fi.programacion2.domain.Menu;
import ar.edu.um.fi.programacion2.domain.Venta;
import ar.edu.um.fi.programacion2.repository.DetalleVentaRepository;
import ar.edu.um.fi.programacion2.repository.VentaRepository;
import ar.edu.um.fi.programacion2.service.DetalleVentaService;
import ar.edu.um.fi.programacion2.service.MenuService;
import ar.edu.um.fi.programacion2.service.VentaService;
import ar.edu.um.fi.programacion2.web.rest.dto.VentaForReporteDTO;
import ar.edu.um.fi.programacion2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.fi.programacion2.domain.Venta}.
 */
@RestController
@RequestMapping("/api")
public class VentaResource {

    private final Logger log = LoggerFactory.getLogger(VentaResource.class);

    private static final String ENTITY_NAME = "venta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VentaService ventaService;

    private final VentaRepository ventaRepository;

    private final MenuService menuService;

    private final DetalleVentaService detalleVentaService;

    @Autowired
    private final DetalleVentaRepository detalleVentaRepository;

    public VentaResource(
        VentaService ventaService,
        VentaRepository ventaRepository,
        MenuService menuService,
        DetalleVentaService detalleVentaService,
        DetalleVentaRepository detalleVentaRepository
    ) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
        this.menuService = menuService;
        this.detalleVentaService = detalleVentaService;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    /**
     * {@code POST  /ventas} : Create a new venta.
     *
     * @param venta the venta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new venta, or with status {@code 400 (Bad Request)} if the venta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ventas")
    public ResponseEntity<Venta> createVenta(@RequestBody Venta venta) throws URISyntaxException {
        log.debug("REST request to save Venta : {}", venta);
        if (venta.getId() != null) {
            throw new BadRequestAlertException("A new venta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Venta result = ventaService.save(venta);
        return ResponseEntity
            .created(new URI("/api/ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ventas/:id} : Updates an existing venta.
     *
     * @param id the id of the venta to save.
     * @param venta the venta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venta,
     * or with status {@code 400 (Bad Request)} if the venta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the venta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ventas/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable(value = "id", required = false) final Long id, @RequestBody Venta venta)
        throws URISyntaxException {
        log.debug("REST request to update Venta : {}, {}", id, venta);
        if (venta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Venta result = ventaService.update(venta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ventas/:id} : Partial updates given fields of an existing venta, field will ignore if it is null
     *
     * @param id the id of the venta to save.
     * @param venta the venta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venta,
     * or with status {@code 400 (Bad Request)} if the venta is not valid,
     * or with status {@code 404 (Not Found)} if the venta is not found,
     * or with status {@code 500 (Internal Server Error)} if the venta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ventas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Venta> partialUpdateVenta(@PathVariable(value = "id", required = false) final Long id, @RequestBody Venta venta)
        throws URISyntaxException {
        log.debug("REST request to partial update Venta partially : {}, {}", id, venta);
        if (venta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Venta> result = ventaService.partialUpdate(venta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venta.getId().toString())
        );
    }

    /**
     * {@code GET  /ventas} : get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventas in body.
     */
    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> getAllVentas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ventas");
        Page<Venta> page = ventaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * @code GET /ventas/:fechaInicio/:fechaFin
     *
     * @param fechaInicio the starting date for query
     * @param fechaFin the ending date for query
     * @return the list of sales between those dates
     * */
    @GetMapping("/ventas/{fechaInicio}/{fechaFin}")
    public ResponseEntity<List<VentaForReporteDTO>> getVentasbetweenDates(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
        List<Venta> ventas = ventaService.findBetweenDate(LocalDateTime.parse(fechaInicio), LocalDateTime.parse(fechaFin));

        System.out.println("\n\n\n" + ventas + "\n\n\n");

        List<VentaForReporteDTO> ventaForReporteDTOList = new ArrayList<>();

        for (Venta venta : ventas) {
            for (DetalleVenta dv : detalleVentaRepository.findByVenta(venta)) {
                System.out.println("\n\n\n" + dv + "\n\n\n");
                ventaForReporteDTOList.add(new VentaForReporteDTO(venta, dv));
            }
        }

        System.out.println("\n\n\n" + ventaForReporteDTOList);

        return ResponseEntity.ok().body(ventaForReporteDTOList);
    }

    /**
     * {@code GET  /ventas/:id} : get the "id" venta.
     *
     * @param id the id of the venta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the venta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ventas/{id}")
    public ResponseEntity<Venta> getVenta(@PathVariable Long id) {
        log.debug("REST request to get Venta : {}", id);
        Optional<Venta> venta = ventaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(venta);
    }

    /**
     * {@code DELETE  /ventas/:id} : delete the "id" venta.
     *
     * @param id the id of the venta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ventas/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        log.debug("REST request to delete Venta : {}", id);
        ventaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/comprar/")
    public ResponseEntity<String> createCompra(@RequestBody Map<String, List<Map<String, Long>>> body) throws URISyntaxException {
        Venta venta = new Venta();
        log.debug("REST request to save Venta : {}", venta);
        if (venta.getId() != null) {
            throw new BadRequestAlertException("A new venta cannot already have an ID", ENTITY_NAME, "idexists");
        }

        List<Map<String, Long>> menuesRaw = body.get("menues");
        AtomicReference<Float> precioTotal = new AtomicReference<>(0F);

        menuesRaw.forEach(m -> { //Obtener el precio total de la venta
            Menu menu = menuService.findOne(m.get("menu")).get();
            precioTotal.updateAndGet(v -> v + menu.getPrecio() * m.get("cantidad"));
            System.out.println("\n" + m.get("cantidad").getClass() + "\n");
        });

        venta.setPrecio(precioTotal.get());
        Instant fecha = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        venta.setFecha(fecha);
        venta.setCodigoSeguimiento(UUID.randomUUID());

        Venta result = ventaService.save(venta); // Guarda el nuevo objeto Venta

        menuesRaw.forEach(m -> {
            DetalleVenta detalleVenta = new DetalleVenta();

            Menu menu = menuService.findOne(m.get("menu")).get();

            Venta ventaAux = ventaService.findOne(result.getId()).get();

            detalleVenta.setCantidad(Math.toIntExact(m.get("cantidad")));

            detalleVenta.setSubtotal(menu.getPrecio() * m.get("cantidad"));

            detalleVenta.setMenu(menu);

            detalleVenta.setVenta(ventaAux);

            detalleVenta.setForeignId(menu.getForeignId());

            detalleVentaService.save(detalleVenta);
        });

        return ResponseEntity
            .created(new URI("/api/ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body("{\"codigoSeguimiento\" : \"" + venta.getCodigoSeguimiento() + "\" }");
    }
}
