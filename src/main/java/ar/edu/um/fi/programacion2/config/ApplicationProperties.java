package ar.edu.um.fi.programacion2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Franchise.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class
    private String reportAPIToken;

    private String mainAPIToken;

    private String mainAPIBaseURL;

    private String reportAPIBaseURL;

    private String reportAPIUsername;

    private String reportAPIPassword;

    private String mainAPIFranquiciaId;

    public String getMainAPIFranquiciaId() {
        return mainAPIFranquiciaId;
    }

    public void setMainAPIFranquiciaId(String mainAPIFranquiciaId) {
        this.mainAPIFranquiciaId = mainAPIFranquiciaId;
    }

    public String getReportAPIUsername() {
        return reportAPIUsername;
    }

    public void setReportAPIUsername(String reportAPIUsername) {
        this.reportAPIUsername = reportAPIUsername;
    }

    public String getReportAPIPassword() {
        return reportAPIPassword;
    }

    public void setReportAPIPassword(String reportAPIPassword) {
        this.reportAPIPassword = reportAPIPassword;
    }

    public String getReportAPIToken() {
        return reportAPIToken;
    }

    public void setReportAPIToken(String reportAPIToken) {
        this.reportAPIToken = reportAPIToken;
    }

    public String getMainAPIToken() {
        return mainAPIToken;
    }

    public void setMainAPIToken(String mainAPIToken) {
        this.mainAPIToken = mainAPIToken;
    }

    public String getMainAPIBaseURL() {
        return mainAPIBaseURL;
    }

    public void setMainAPIBaseURL(String mainAPIBaseURL) {
        this.mainAPIBaseURL = mainAPIBaseURL;
    }

    public String getReportAPIBaseURL() {
        return reportAPIBaseURL;
    }

    public void setReportAPIBaseURL(String reportAPIBaseURL) {
        this.reportAPIBaseURL = reportAPIBaseURL;
    }
}
