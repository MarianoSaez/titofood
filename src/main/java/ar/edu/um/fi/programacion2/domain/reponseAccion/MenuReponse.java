package ar.edu.um.fi.programacion2.domain.reponseAccion;

import ar.edu.um.fi.programacion2.domain.Menu;
import ar.edu.um.fi.programacion2.service.dto.MenuDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuReponse {

    private List<MenuDTO> menus;

    public List<MenuDTO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDTO> menus) {
        this.menus = menus;
    }
}
