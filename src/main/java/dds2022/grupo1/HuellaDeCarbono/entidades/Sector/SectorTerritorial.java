package dds2022.grupo1.HuellaDeCarbono.entidades.Sector;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoImplementadoException;
import javax.persistence.*;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"nombre"}) })
public class SectorTerritorial extends Persistente {
    @Column
    private String nombre;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    private SectorTerritorial provincia;

    public SectorTerritorial(String nombre) {
        this.nombre = nombre;
    }

    public SectorTerritorial() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float calcularHC() throws NoImplementadoException {
        throw new NoImplementadoException("asd");

    }

    @Override
    public String toString(){
        return nombre;
    }

}
