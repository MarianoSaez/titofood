package ar.edu.um.fi.programacion2.service.dto;

import ar.edu.um.fi.programacion2.service.dto.types.TipoReporte;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDTO {

    //    private Long id;
    private TipoReporte tipo;
    private ZonedDateTime fechaInicio;
    private ZonedDateTime fechaFin;
    private String intervalo;

    private Long foreignId;

    private Long reporteCanceladoId;

    public ReporteDTO() {}

    //    public Long getId() {
    //        return id;
    //    }
    //
    //    public void setId(Long id) {
    //        this.id = id;
    //    }

    public Long getReporteCanceladoId() {
        return reporteCanceladoId;
    }

    public void setReporteCanceladoId(Long reporteCanceladoId) {
        this.reporteCanceladoId = reporteCanceladoId;
    }

    @JsonProperty("foreignId")
    public Long getForeignId() {
        return foreignId;
    }

    @JsonProperty("id")
    public void setForeignId(Long foreignId) {
        this.foreignId = foreignId;
    }

    public TipoReporte getTipo() {
        return tipo;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(ZonedDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ZonedDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }
}
