package ar.edu.um.fi.programacion2.asyncTasks;

import ar.edu.um.fi.programacion2.asyncTasks.dto.AuthDTO;
import ar.edu.um.fi.programacion2.asyncTasks.dto.TokenDTO;
import ar.edu.um.fi.programacion2.config.ApplicationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@EnableAsync
@EnableScheduling
public class ReportServiceTokenRefresher {

    private Logger log = LoggerFactory.getLogger(ReportServiceTokenRefresher.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Async
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public void refreshToken() throws IOException {
        // Preparar los headers y el request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(
            new ObjectMapper()
                .writeValueAsString(
                    new AuthDTO(applicationProperties.getReportAPIUsername(), applicationProperties.getReportAPIPassword())
                ),
            headers
        );
        RestTemplate restTemplate = new RestTemplate();

        log.info("Refreshing Reports Service token");
        TokenDTO tokenReponse = restTemplate.postForObject(
            applicationProperties.getReportAPIBaseURL() + "/authenticate",
            request,
            TokenDTO.class
        );

        if (tokenReponse != null) {
            applicationProperties.setReportAPIToken(tokenReponse.getId_token());
            log.info("Token updated successfully");
        } else {
            log.error("Failed to refresh token");
        }
    }
}
