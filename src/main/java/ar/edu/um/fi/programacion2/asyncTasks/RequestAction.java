package ar.edu.um.fi.programacion2.asyncTasks;

import ar.edu.um.fi.programacion2.domain.reponseAccion.AbstractResponseAccion;
import ar.edu.um.fi.programacion2.domain.reponseAccion.MenuReponse;
import ar.edu.um.fi.programacion2.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@EnableScheduling
public class RequestAction {

    private Logger log = LoggerFactory.getLogger(RequestAction.class);

    @Autowired
    private final MenuService menuService;

    public RequestAction(MenuService menuService) {
        this.menuService = menuService;
    }

    @Async
    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void request() throws IOException {
        log.info("Calling Main service for actions");

        // Perform action request
        URL url = new URL("http://10.101.102.1:8080/api/accion");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty(
            "Authorization",
            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0aXRvZm9vZCIsImF1dGgiOiIiLCJleHAiOjE5ODg4MjgwNTR9.4_P9ZyGuNCp8bwdKWsC22MJn4NGlpjJDcvvdg-UEwHhdyAylJ03qnGE6DJh2xdWYeQcnvMkjFgnzRK5sfn9sJQ"
        );
        con.setDoOutput(true);
        con.setConnectTimeout(50000);
        con.setReadTimeout(50000);

        String jsonInputString = "{\"accion\": \"consulta\", \"franquiciaID\": \"0276c5be-9663-4b8a-aaf4-736db649a2de\"}";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        String serializedResponse;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            serializedResponse = response.toString();
        }

        // Parse response
        AbstractResponseAccion responseAccion = new ObjectMapper().readValue(serializedResponse, AbstractResponseAccion.class);
        log.info("Response from main service : {}", responseAccion.getAccion());

        // Take action if needed
        if (Objects.equals(responseAccion.getAccion(), "menu")) {
            // Save new menu
            MenuReponse menuAccion = new ObjectMapper().readValue(serializedResponse, MenuReponse.class);
            menuService.updateActiveMenus(menuAccion.getMenus());
        } else if (Objects.equals(responseAccion.getAccion(), "reporte")) {
            // Check the type

        }
    }
}
