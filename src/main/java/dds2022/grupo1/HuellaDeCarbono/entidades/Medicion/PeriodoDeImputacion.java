package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.enums.Mes;
import dds2022.grupo1.HuellaDeCarbono.services.LogCalculos;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "mes", "anio" }) })
@NamedQuery(name = "getAllPeriodoDeImputacion", query = "select p from PeriodoDeImputacion p")
@NamedNativeQuery(name = "truncatePeriodoDeImputacion", query = "truncate table PeriodoDeImputacion")
@NamedQuery(query = "Select m from PeriodoDeImputacion m where m.anio = :anio and m.mes = :mes", name = "find periodo by anio and mes")
public class PeriodoDeImputacion extends Persistente {
    @Enumerated(EnumType.STRING)
    private Mes mes;
    @Column
    private int anio;
    @OneToMany(mappedBy = "periododeimputacion", cascade = CascadeType.REMOVE)
    private List<LogCalculos> logCalculos = new ArrayList<>();

    public PeriodoDeImputacion(Mes mes, int anio) {
        this.mes = mes;
        this.anio = anio;
    }

    public PeriodoDeImputacion(int anio) {
        this.anio = anio;
    }

    public PeriodoDeImputacion() {
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return (mes != null ? (mes.getValor() < 10 ? "0" + Integer.toString(mes.getValor())
                : Integer.toString(mes.getValor()))
                + Integer.toString(anio)
                : Integer.toString(anio));
    }

    public boolean esMismoPeriodo(PeriodoDeImputacion periodo) {
        return esMismoAnio(periodo) & esMismoMes(periodo);
    }

    public boolean esMismoAnio(PeriodoDeImputacion periodo) {
        return anio == periodo.getAnio();
    }

    public boolean esMismoMes(PeriodoDeImputacion periodo) {
        return mes == periodo.getMes();
    }
}
