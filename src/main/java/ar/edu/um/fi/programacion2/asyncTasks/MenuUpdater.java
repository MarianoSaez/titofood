package ar.edu.um.fi.programacion2.asyncTasks;

import ar.edu.um.fi.programacion2.domain.Menu;
import ar.edu.um.fi.programacion2.service.MenuService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class MenuUpdater {

    @Autowired
    private MenuService menuService;

    public void update(List<Menu> menus) {}
}
