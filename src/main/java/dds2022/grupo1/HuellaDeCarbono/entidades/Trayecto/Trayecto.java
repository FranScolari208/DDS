package dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.ActividadRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Medicion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

import javax.persistence.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NamedQuery(name = "find trayectos by member", query = "select a from Trayecto a where ")
@NamedQuery(name = "getAllTrayecto", query = "select a from Trayecto a")
@NamedQuery(name = "find trayecto by id", query = "select a from Trayecto a where a.id = :id")
@NamedQuery(name = "editar trayecto", query = "update Trayecto a set a.destino = :ubicacionDestino, a.salida = :ubicacionSalida, a.medioDeTransporte = :transporte, a.cantidadParticipantes = :cantidadParticipantes where a.id = :id")
public class Trayecto extends Persistente {
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "salida_id", referencedColumnName = "id")
    private Ubicacion salida;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "destino_id", referencedColumnName = "id")
    private Ubicacion destino;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "medio_de_transporte", referencedColumnName = "id")
    private MedioDeTransporte medioDeTransporte;
    @OneToMany(mappedBy = "trayecto", cascade = CascadeType.REMOVE)
    private List<Tramo> tramos = new ArrayList<>();
    @Column
    private Integer cantidadParticipantes = 1;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "periododeimputacion_id", referencedColumnName = "id")
    private PeriodoDeImputacion periodoDeImputacion;
    @ManyToMany(mappedBy = "trayectos", cascade = CascadeType.REMOVE)
    private List<Miembro> miembros = new ArrayList<>();

    public Trayecto(Ubicacion salida, Ubicacion destino, MedioDeTransporte medioDeTransporte, List<Tramo> tramos,
            int i) {
        this.salida = salida;
        this.destino = destino;
        this.medioDeTransporte = medioDeTransporte;
        this.tramos = tramos;
        this.cantidadParticipantes = i;
    }

    @Override
    public String toString(){
        JSONObject json = new JSONObject()
        .put("id", id)
        .put("salida", new JSONObject(salida.toString()))
        .put("destino", new JSONObject(destino.toString()))
        .put("cantidadParticipantes", cantidadParticipantes)
        .put("transporte", medioDeTransporte.toString());
        if(periodoDeImputacion!= null) json.put("periodoDeImputacion", periodoDeImputacion.toString());

        JSONArray miembrosJson = new JSONArray();
        miembros.forEach(miembro -> {
            miembrosJson.put(new JSONObject(miembro.toString()));
        });
        json.put("miembros", miembrosJson);

        return json.toString();
    }

    public Trayecto() {
    }

    public Trayecto(Ubicacion salida, Ubicacion destino, MedioDeTransporte medioDeTransporte, List<Tramo> tramos, int i,
            PeriodoDeImputacion periodo) {
        this.salida = salida;
        this.destino = destino;
        this.medioDeTransporte = medioDeTransporte;
        this.tramos = tramos;
        this.cantidadParticipantes = i;
        this.periodoDeImputacion = periodo;
    }

    public Ubicacion getSalida() {
        return salida;
    }

    public void setSalida(Ubicacion salida) {
        this.salida = salida;
    }

    public Ubicacion getDestino() {
        return destino;
    }

    public void setDestino(Ubicacion destino) {
        this.destino = destino;
    }

    public MedioDeTransporte getMedioDeTransporte() {
        return medioDeTransporte;
    }

    public void setMedioDeTransporte(MedioDeTransporte medioDeTransporte) {
        this.medioDeTransporte = medioDeTransporte;
    }

    public void setPeriodoDeImputacion(PeriodoDeImputacion periodo) {
        this.periodoDeImputacion = periodo;
    }

    public List<Tramo> getTramos() {
        return tramos;
    }

    public void setTramos(List<Tramo> tramos) {
        this.tramos = tramos;
    }

    public Integer getCantidadParticipantes() {
        return cantidadParticipantes;
    }

    public PeriodoDeImputacion getPeriodoDeImputacion() {
        return periodoDeImputacion;
    }

    public void setCantidadParticipantes(Integer cantidadParticipantes) {
        this.cantidadParticipantes = cantidadParticipantes;
    }

    public List<Miembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Miembro> miembros) {
        this.miembros = miembros;
    }

    public float calcularDistancia() {
        return medioDeTransporte.calcularDistancia(tramos);
    }

    public void addMiembro(Miembro miembro) {
        miembros.add(miembro);
    }

    public float calcularHC() throws NoExisteException {
        Actividad actividad = ActividadRepositorio.getInstancia()
                .getActividad("Traslado de Miembros de la Organizacion");// , Alcance.OTRAS_EMISIONES);
        Float a = calcularDistancia();
        Consumo consumo = medioDeTransporte.calcularConsumo(a);
        Medicion medicionTrayecto = new Medicion(actividad, consumo, periodoDeImputacion);
        return medicionTrayecto.calcularHC();
    }

    public void armarMedicion() {
    }

    public float calcularHCDivididoParticipantes() {

        return this.calcularHC() / cantidadParticipantes;
    }

}
