package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;

import javax.persistence.*;

import org.json.JSONObject;

@Entity
@DiscriminatorValue("MEDICION")
@NamedQuery(name = "getAllMedicion", query = "select a from Medicion a")
@Inheritance
public class Medicion extends Medible {

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "actividad_id", referencedColumnName = "id")
    private Actividad actividad;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "consumo_id", referencedColumnName = "id")
    private Consumo consumo;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "periododeimputacion_id", referencedColumnName = "id")
    private PeriodoDeImputacion periodoImputacion;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private Organizacion organizacion;

    public Medicion(Actividad actividad, Consumo consumo, PeriodoDeImputacion periodoImputacion) {
        this.actividad = actividad;
        this.consumo = consumo;
        this.periodoImputacion = periodoImputacion;
    }

    public Medicion(Actividad actividad, Consumo consumo, PeriodoDeImputacion periodoImputacion,
            Organizacion organizacion) {
        this.actividad = actividad;
        this.consumo = consumo;
        this.periodoImputacion = periodoImputacion;
        this.organizacion = organizacion;
    }

    public Medicion() {
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public void setConsumo(Consumo consumo) {
        this.consumo = consumo;
    }
    public Organizacion getOrganizacion() {
        return organizacion;
    }
    public void setOrganizacion(Organizacion org) {
        this.organizacion = org;
    }

    @Override
    public PeriodoDeImputacion getPeriodoImputacion() {
        return periodoImputacion;
    }

    public void setPeriodoImputacion(PeriodoDeImputacion periodoImputacion) {
        this.periodoImputacion = periodoImputacion;
    }

    public Float calcularHC() {
        return consumo.calcularHC();
    }

    @Override
    public String getUnidad() {
        return consumo.getTipoConsumo().getUnidad().toString();
    }

    @Override
    public Double getValor() {
        return (double) consumo.getValor();
    }

    @Override
    public Consumo getConsumo() {
        return consumo;
    }

    @Override
    public String getCategoria() {
        // nombre tipo consumo??
        return consumo.getTipoConsumo().getNombre();
    }

    @Override
    public String toString(){
        JSONObject json = new JSONObject()
        .put("id", id)
        .put("actividad", new JSONObject(actividad.toString()))
        .put("consumo", new JSONObject(consumo.toString()))
        .put("periodo_imputacion", periodoImputacion.toString());
        if (organizacion != null) json.put("organizacion_id", organizacion.getId());
/*

    private Actividad actividad;

    private Consumo consumo;

    private PeriodoDeImputacion periodoImputacion;

    private Organizacion organizacion;
*/
        return json.toString();
    }


}
