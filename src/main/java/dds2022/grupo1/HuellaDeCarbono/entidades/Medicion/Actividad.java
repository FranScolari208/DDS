package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.enums.Alcance;

import javax.persistence.*;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO: cambiar el many to one a un many to many
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"nombre", "sector_id"}) })
@NamedQuery(name = "getAllActividad", query = "select a from Actividad a")
public class Actividad extends Persistente {
    @Enumerated(EnumType.STRING)
    private Alcance alcance;

    @Column
    private String nombre;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "sector_id", referencedColumnName = "id")
    private Sector sector;

    public Actividad(String nombre, Alcance alcance) {
        this.nombre = nombre;
        this.alcance = alcance;
    }

    public Actividad(String nombre, Alcance alcance, Sector sector) {
        this.alcance = alcance;
        this.nombre = nombre;
        this.sector = sector;
    }

    public Actividad() {
    }

    public String getNombre() {
        return nombre;
    }

    public Alcance getAlcance() {
        return alcance;
    }
    public Sector getSector() {
        return sector;
    }
    public void setSector(Sector sector) {
        this.sector=sector;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAlcance(Alcance alcance) {
        this.alcance = alcance;
    }

    public boolean esMismaActividad(Actividad actividad) {
        return this.getNombre().equals(actividad.getNombre());
    }

    public List<Medible> medicionesFiltradas(List<Medible> mediciones){
        List<Medible> medicionesfiltradas = new ArrayList<>();
        medicionesfiltradas = (List<Medible>) mediciones.stream()
                .filter(medicion -> medicion.getActividad().esMismaActividad(this)).collect(Collectors.toList());

        return medicionesfiltradas;}

    public float obtenerHC(List<Medible> mediciones, Organizacion org, List<String> nombres) {

        List<Medible> medicionesfiltradas = new ArrayList<>();
        medicionesfiltradas = (List<Medible>) mediciones.stream()
                .filter(medicion -> medicion.getActividad().esMismaActividad(this)).collect(Collectors.toList());
        return org.obtenerHUXActividad(medicionesfiltradas, nombres);
    }



    @Override
    public String toString(){
        JSONObject json = new JSONObject()
        .put("id", id)
        .put("nombre", nombre)
        .put("alcance", alcance.toString());
        return json.toString();
    }
}