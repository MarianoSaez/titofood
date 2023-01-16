package ar.edu.um.fi.programacion2.domain.reponseAccion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractResponseAccion {

    protected String accion;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
}
