package ar.edu.um.fi.programacion2.service.dto.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TipoReporte {
    @JsonProperty("recurrente")
    RECURR("recurrente"),

    @JsonProperty("cancelar")
    CANCELAR("cancelar"),

    @JsonProperty("historico")
    HIST("historico");

    private final String value;

    TipoReporte(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
