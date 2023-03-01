package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table() // uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre" }) })
public class TipoTransporte {
    public TipoTransporte() {
    }

    @Id
    @GeneratedValue
    private int id;

    @Column
    String nombre;

    public TipoTransporte(String nomString) {
        nombre = nomString;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
