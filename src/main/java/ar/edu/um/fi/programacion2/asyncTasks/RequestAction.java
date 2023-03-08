package ar.edu.um.fi.programacion2.asyncTasks;

import ar.edu.um.fi.programacion2.domain.Menu;
import ar.edu.um.fi.programacion2.domain.reponseAccion.AbstractResponseAccion;
import ar.edu.um.fi.programacion2.domain.reponseAccion.MenuReponse;
import ar.edu.um.fi.programacion2.domain.reponseAccion.ReporteResponse;
import ar.edu.um.fi.programacion2.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0aXRvZm9vZCIsImF1dGgiOiIiLCJleHAiOjE5OTI3NzAwMjN9.j8neTNht5GBgeYurRPyKSGsIYfQ2n5wUgdlTl9NvL_8pAJkZqd_OSenoCals2M79wCb9Nu4vvBhgtkUeEInKWw"
        );
        con.setDoOutput(true);
        con.setConnectTimeout(50000);
        con.setReadTimeout(50000);

        String jsonInputString = "{\"accion\": \"consulta\", \"franquiciaID\": \"40e8be94-6bb4-4bd7-a81a-14a82167de5e\"}";

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

            URL reporteUrl = new URL("http://127.0.1.1:8081/api/reportes");
            HttpURLConnection reporteConn = (HttpURLConnection) reporteUrl.openConnection();
            reporteConn.setRequestMethod("POST");
            reporteConn.setRequestProperty("Content-Type", "application/json");
            reporteConn.setRequestProperty("Accept", "application/json");
            reporteConn.setRequestProperty(
                "Authorization",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmcmFucXVpY2lhIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY3ODI4NDczNX0.CijB_KMB6sazYXwN7Wm-DEjN2Wrvt23OoLNPBLhfY5wT0BiCBb0FVGL7lmGulfeKCmOKoLfvQFouZYBhT7yHqg"
            );
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
