package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;

import javax.persistence.*;

import org.json.JSONObject;

@Entity
@Table
@NamedQuery(name = "getAllFactorEmision", query = "select a from FactorEmision a")
public class FactorEmision extends Persistente {
    @Column(name = "valorFactorEmision")
    private double valor;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "unidadFactorEmision_id", referencedColumnName = "id")
    private Unidad unidad;

    public FactorEmision() {
    }

    @Override
    public String toString(){
        return new JSONObject()
        .put("id", id)
        .put("valor", valor)
        .put("unidad", unidad.toString())
        .toString();
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public FactorEmision(double valor) {
        this.valor = valor;
        this.unidad = new Unidad("GEC02EQ", new Unidad("M3"));
    }// ESTO NO TENDRIA QUE EXISTIR

    public FactorEmision(double valor, Unidad unidad) {
        this.valor = valor;
        this.unidad = unidad;
    }

    // Pensar metodo que haga las conversiones de unidad

    public void convertirUnidad() {
    }
}
