package ar.edu.um.fi.programacion2.asyncTasks;

import ar.edu.um.fi.programacion2.config.ApplicationProperties;
import ar.edu.um.fi.programacion2.domain.Menu;
import ar.edu.um.fi.programacion2.domain.reponseAccion.AbstractResponseAccion;
import ar.edu.um.fi.programacion2.domain.reponseAccion.MenuReponse;
import ar.edu.um.fi.programacion2.domain.reponseAccion.ReporteResponse;
import ar.edu.um.fi.programacion2.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
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

    @Autowired
    private final ApplicationProperties applicationProperties;

    public RequestAction(MenuService menuService, ApplicationProperties applicationProperties) {
        this.menuService = menuService;
        this.applicationProperties = applicationProperties;
    }

    @Async
    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void request() throws IOException {
        log.info("Calling Main service at {} for actions", applicationProperties.getMainAPIBaseURL());

        // Perform action request
        URL url = new URL(applicationProperties.getMainAPIBaseURL() + "/accion");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + applicationProperties.getMainAPIToken());
        con.setDoOutput(true);
        con.setConnectTimeout(50000);
        con.setReadTimeout(50000);

        String jsonInputString = "{\"accion\": \"consulta\", \"franquiciaID\": \"" + applicationProperties.getMainAPIFranquiciaId() + "\"}";

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
            List<Menu> l = menuService.updateActiveMenus(menuAccion.getMenus());
            log.info("Menus updated successfully : {}", l);
        } else if (Objects.equals(responseAccion.getAccion(), "reporte")) {
            // Pass the report action to the reports service. It will handle it by itself
            ReporteResponse reporteAccion = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                //                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .readValue(serializedResponse, ReporteResponse.class);
            String jsonReport = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                //                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .writeValueAsString(reporteAccion.getReporte());

            log.error("{}", jsonReport);

            URL reporteUrl = new URL(applicationProperties.getReportAPIBaseURL() + "/reportes");
            HttpURLConnection reporteConn = (HttpURLConnection) reporteUrl.openConnection();
            reporteConn.setRequestMethod("POST");
            reporteConn.setRequestProperty("Content-Type", "application/json");
            reporteConn.setRequestProperty("Accept", "application/json");
            reporteConn.setRequestProperty("Authorization", "Bearer " + applicationProperties.getReportAPIToken());
            reporteConn.setDoOutput(true);

            try (OutputStream os = reporteConn.getOutputStream()) {
                byte[] input = jsonReport.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String s;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(reporteConn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                s = response.toString();
            }

            log.error("{}", s);
        }
    }
}
