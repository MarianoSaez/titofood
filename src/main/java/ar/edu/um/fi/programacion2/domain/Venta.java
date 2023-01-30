package ar.edu.um.fi.programacion2.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "precio")
    private Float precio;

    @Column(name = "foreign_id")
    private Double foreignId;

    @ManyToMany(mappedBy = "ventas", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ventas" }, allowSetters = true)
    private Set<Menu> menus = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Venta fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public Venta precio(Float precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Double getForeignId() {
        return this.foreignId;
    }

    public Venta foreignId(Double foreignId) {
        this.setForeignId(foreignId);
        return this;
    }

    public void setForeignId(Double foreignId) {
        this.foreignId = foreignId;
    }

    public Set<Menu> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menu> menus) {
        if (this.menus != null) {
            this.menus.forEach(i -> i.removeVenta(this));
        }
        if (menus != null) {
            menus.forEach(i -> i.addVenta(this));
        }
        this.menus = menus;
    }

    public Venta menus(Set<Menu> menus) {
        this.setMenus(menus);
        return this;
    }

    public Venta addMenu(Menu menu) {
        this.menus.add(menu);
        menu.getVentas().add(this);
        return this;
    }

    public Venta removeMenu(Menu menu) {
        this.menus.remove(menu);
        menu.getVentas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", precio=" + getPrecio() +
            ", foreignId=" + getForeignId() +
            "}";
    }
}
