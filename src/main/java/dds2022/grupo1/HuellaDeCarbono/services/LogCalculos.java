package dds2022.grupo1.HuellaDeCarbono.services;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@NamedQuery(name = "getAllLogCalculos", query = "select a from LogCalculos a")
@NamedNativeQuery(name = "truncateLogCalculos", query = "truncate table LogCalculos")
public class LogCalculos extends Persistente {
    @Column
    public float calculoHC;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    public Organizacion organizacion;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "periododeimputacion_id", referencedColumnName = "id")
    public PeriodoDeImputacion periododeimputacion;
    @Column
    public LocalDateTime date = LocalDateTime.now();

    public float getCalculoHC() {
        return calculoHC;
    }

    public void setCalculoHC(float calculoHC) {
        this.calculoHC = calculoHC;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public PeriodoDeImputacion getPeriododeimputacion() {
        return periododeimputacion;
    }

    public void setPeriododeimputacion(PeriodoDeImputacion periododeimputacion) {
        this.periododeimputacion = periododeimputacion;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
