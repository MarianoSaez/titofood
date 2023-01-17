package ar.edu.um.fi.programacion2.service.dto;

import ar.edu.um.fi.programacion2.domain.Menu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDTO implements Serializable {

    private Long id;
    private String nombre;
    private Float precio;
    private String descripcion;
    private String urlImagen;

    @JsonProperty("activo")
    private Boolean isActive;

    private Double foreignId;
    private String creado;
    private String actualizado;

    @Override
    public String toString() {
        return (
            "MenuDTO{" +
            "id=" +
            id +
            ", nombre='" +
            nombre +
            '\'' +
            ", precio=" +
            precio +
            ", descripcion='" +
            descripcion +
            '\'' +
            ", urlImagen='" +
            urlImagen +
            '\'' +
            ", isActive=" +
            isActive +
            ", foreignId=" +
            foreignId +
            ", creado='" +
            creado +
            '\'' +
            ", actualizado='" +
            actualizado +
            '\'' +
            '}'
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Double getForeignId() {
        return foreignId;
    }

    public void setForeignId(Double foreignId) {
        this.foreignId = foreignId;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public String getActualizado() {
        return actualizado;
    }

    public void setActualizado(String actualizado) {
        this.actualizado = actualizado;
    }

    public MenuDTO() {
        // Empty constructor needed for Jackson
    }

    public MenuDTO(Menu menu) {
        this.id = menu.getId();
        this.nombre = menu.getNombre();
        this.precio = menu.getPrecio();
        this.descripcion = menu.getDescripcion();
        this.urlImagen = menu.getUrlImagen();
        this.isActive = menu.getIsActive();
        this.foreignId = menu.getForeignId();
        this.creado = menu.getCreado();
        this.actualizado = menu.getActualizado();
    }
}
