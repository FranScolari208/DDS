package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"nombre", "unidadDividendo_id"}) })
@NamedQuery(name = "getAllUnidad", query = "select a from Unidad a")
public class Unidad extends Persistente {
    @Column()
    private String nombre;
    // @Column()
    // @JoinColumn(name = "unidadDividendo", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "unidadDividendo_id", referencedColumnName = "id")
    private Unidad unidadDividendo;

    public Unidad(String nombre, Unidad unidadDividendo) {
        this.nombre = nombre;
        this.unidadDividendo = unidadDividendo;
    }

    public Unidad(String nombre) {
        this.nombre = nombre;
        this.unidadDividendo = null;
    }

    public Unidad() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUnidadDividendo(Unidad unidadDividendo) {
        this.unidadDividendo = unidadDividendo;
    }

    public String getNombre() {
        return nombre;
    }

    public Unidad getUnidadDividendo() {
        return unidadDividendo;
    }

    /*
     * public Unidad multiplicar(Unidad unaUnidad){
     * if(unaUnidad.getUnidadDividendo() != null){
     * if(nombre.equals(unaUnidad.getUnidadDividendo().getNombre())){
     * return unaUnidad;
     * }
     * }
     * };
     */
    @Override
    public String toString() {
        if (unidadDividendo != null) {
            return String.format("%s/%s", nombre, unidadDividendo.toString());
        }
        return nombre;
    }

}
