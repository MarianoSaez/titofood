package ar.edu.um.fi.programacion2.web.rest.dto;

import ar.edu.um.fi.programacion2.domain.DetalleVenta;
import ar.edu.um.fi.programacion2.domain.Venta;
import java.time.Instant;

public class VentaForReporteDTO {

    private Instant fecha;
    private Float precio;
    private Long foreignId;

    public VentaForReporteDTO(Venta v, DetalleVenta dv) {
        this.fecha = v.getFecha();
        this.precio = dv.getSubtotal();
        this.foreignId = dv.getForeignId().longValue();
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public void setForeignId(Long foreignId) {
        this.foreignId = foreignId;
    }
}
