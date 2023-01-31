package ar.edu.um.fi.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.fi.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetalleVentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleVenta.class);
        DetalleVenta detalleVenta1 = new DetalleVenta();
        detalleVenta1.setId(1L);
        DetalleVenta detalleVenta2 = new DetalleVenta();
        detalleVenta2.setId(detalleVenta1.getId());
        assertThat(detalleVenta1).isEqualTo(detalleVenta2);
        detalleVenta2.setId(2L);
        assertThat(detalleVenta1).isNotEqualTo(detalleVenta2);
        detalleVenta1.setId(null);
        assertThat(detalleVenta1).isNotEqualTo(detalleVenta2);
    }
}
