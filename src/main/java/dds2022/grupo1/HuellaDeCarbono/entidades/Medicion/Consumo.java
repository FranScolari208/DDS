package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.enums.Periodicidad;

import javax.persistence.*;

import org.json.JSONObject;

@Table
@Entity
@NamedQuery(name = "getAllConsumo", query = "select a from Consumo a")

public class Consumo extends Persistente {
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "tipoConsumo_id", referencedColumnName = "id")
    private TipoConsumo tipoConsumo;
    @Column
    private Float valor;
    @Enumerated(EnumType.STRING)
    private Periodicidad periodicidad;

    public Consumo() {

    }

    // private Unidad unidad; // Unidad.KG; //cambiar dsp

    public Consumo(TipoConsumo tipoConsumo, Float valor, Periodicidad periodicidad) {// , Unidad unidad) {
        this.tipoConsumo = tipoConsumo;
        this.valor = valor;
        this.periodicidad = periodicidad;
        // this.unidad = unidad;
    }

    public TipoConsumo getTipoConsumo() {
        return tipoConsumo;
    }

    public Float getValor() {
        return valor;
    }

    public Periodicidad getPeriodicidad() {
        return periodicidad;
    }

    public void setTipoConsumo(TipoConsumo tipoConsumo) {
        this.tipoConsumo = tipoConsumo;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public void setPeriodicidad(Periodicidad periodicidad) {
        this.periodicidad = periodicidad;
    }

    public Float calularDatosActividad() {

        if (periodicidad == Periodicidad.ANUAL) {
            return valor * 24 * 365;
        } else if (periodicidad == Periodicidad.MENSUAL){
            return valor * 24 * 30;
        }
        return valor * 24 ;
    }

    public Float calcularHC() {

        FactorEmision factorEmision = tipoConsumo.convertirUnidad(); // llamar al proceso de tipo consumo

        return calularDatosActividad() * (float) factorEmision.getValor();
    }

    @Override
    public String toString(){
        return new JSONObject()
        .put("id", id)
        .put("valor", valor)
        .put("periodicidad", periodicidad.toString())
        .put("tipo_consumo", new JSONObject(tipoConsumo.toString()))
        .toString();
    }
}
