package ar.edu.um.fi.programacion2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Menu.
 */
@Entity
@Table(name = "menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio")
    private Float precio;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "url_imagen")
    private String urlImagen;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "foreign_id")
    private Double foreignId;

    @Column(name = "creado")
    private String creado;

    @Column(name = "actualizado")
    private String actualizado;

    @ManyToMany
    @JoinTable(name = "rel_menu__venta", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "venta_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menus" }, allowSetters = true)
    private Set<Venta> ventas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Menu nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public Menu precio(Float precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Menu descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImagen() {
        return this.urlImagen;
    }

    public Menu urlImagen(String urlImagen) {
        this.setUrlImagen(urlImagen);
        return this;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Menu isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getForeignId() {
        return this.foreignId;
    }

    public Menu foreignId(Double foreignId) {
        this.setForeignId(foreignId);
        return this;
    }

    public void setForeignId(Double foreignId) {
        this.foreignId = foreignId;
    }

    public String getCreado() {
        return this.creado;
    }

    public Menu creado(String creado) {
        this.setCreado(creado);
        return this;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public String getActualizado() {
        return this.actualizado;
    }

    public Menu actualizado(String actualizado) {
        this.setActualizado(actualizado);
        return this;
    }

    public void setActualizado(String actualizado) {
        this.actualizado = actualizado;
    }

    public Set<Venta> getVentas() {
        return this.ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        this.ventas = ventas;
    }

    public Menu ventas(Set<Venta> ventas) {
        this.setVentas(ventas);
        return this;
    }

    public Menu addVenta(Venta venta) {
        this.ventas.add(venta);
        venta.getMenus().add(this);
        return this;
    }

    public Menu removeVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.getMenus().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", precio=" + getPrecio() +
            ", descripcion='" + getDescripcion() + "'" +
            ", urlImagen='" + getUrlImagen() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", foreignId=" + getForeignId() +
            ", creado='" + getCreado() + "'" +
            ", actualizado='" + getActualizado() + "'" +
            "}";
    }
}
