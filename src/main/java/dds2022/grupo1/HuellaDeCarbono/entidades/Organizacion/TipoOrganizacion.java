package dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "descripcion" }) })
@NamedQuery(name = "getAllTipoOrganizacion", query = "select a from TipoOrganizacion a")

public class TipoOrganizacion extends Persistente {
    public TipoOrganizacion() {
    }

    @Column
    private String descripcion;

    public TipoOrganizacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString(){
        return descripcion;
    }
}
