package ar.edu.um.fi.programacion2.domain.reponseAccion;

import ar.edu.um.fi.programacion2.service.dto.MenuDTO;
import ar.edu.um.fi.programacion2.service.dto.ReporteDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteResponse {

    private String accion;

    private ReporteDTO reporte;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public ReporteDTO getReporte() {
        return reporte;
    }

    public void setReporte(ReporteDTO reporte) {
        this.reporte = reporte;
    }

    @Override
    public String toString() {
        return "ReporteResponse{" + "accion='" + accion + '\'' + ", reporte=" + reporte + '}';
    }
}
//package ar.edu.um.fi.programacion2.domain.reponseAccion;
//
//    import ar.edu.um.fi.programacion2.domain.Menu;
//    import ar.edu.um.fi.programacion2.service.dto.MenuDTO;
//    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//    import java.util.List;
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class MenuReponse {
//
//    private List<MenuDTO> menus;
//
//    public List<MenuDTO> getMenus() {
//        return menus;
//    }
//
//    public void setMenus(List<MenuDTO> menus) {
//        this.menus = menus;
//    }
//}
