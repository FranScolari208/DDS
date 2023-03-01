package dds2022.grupo1.HuellaDeCarbono.Interfaces;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@NamedQuery(name = "getAllMedible", query = "select m from Medible m")
@NamedQuery(query = "Select m from Medible m where m.organizacion = :id", name = "find organizacion by id")
@NamedQuery(query = "Select m from Medible m where m.consumo = :id", name = "find consumo by id")
@NamedQuery(query = "Select m from Medible m join Sector s on m.organizacion = s.organizacion where m.organizacion = :id", name = "find sector by id")
public abstract class Medible extends Persistente {

    public Medible() {
    }

    public String getUnidad() {
        return null;
    }

    public Double getValor() {
        return null;
    }

    public String getCategoria() {
        return null;
    }

    public Actividad getActividad() {
        return null;
    }

    // si no se puede hacer esto hay que castear
    // los medibles a mediciones para poder acceder al consumo como linea 77 test
    // application
    public Consumo getConsumo() {
        return null;
    }

    public PeriodoDeImputacion getPeriodoImputacion() {
        return null;
    };

}
