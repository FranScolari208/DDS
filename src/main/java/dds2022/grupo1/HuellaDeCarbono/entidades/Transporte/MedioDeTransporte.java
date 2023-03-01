package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Adaptador;
import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.enums.Periodicidad;
import dds2022.grupo1.HuellaDeCarbono.services.ServicioGeoref;

import javax.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "getAllMedioDeTransporte", query = "select a from MedioDeTransporte a")
@NamedNativeQuery(name = "truncateMedioDeTransporte", query = "truncate table MedioDeTransporte")
public abstract class MedioDeTransporte extends Persistente {
    @Transient
    Adaptador adaptador = new ServicioGeoref();
    @Column
    private String descripcion;

    public MedioDeTransporte() {

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public MedioDeTransporte(String descripcion) {
        this.descripcion = descripcion;
    }

    public float calcularDistancia(List<Tramo> tramos) {
        return (float) tramos.stream().mapToDouble(
                tramo -> adaptador.distancia(
                        tramo.getUbicacionActual(),
                        tramo.getUbicacionProxima()))
                .sum();
    }

    public Consumo calcularConsumo(float distanciaRecorrida) {
        TipoConsumo tipoConsumo = TipoConsumoRepositorio.getInstancia().getTipoConsumo("Distancia media recorrida");
        return new Consumo(tipoConsumo, distanciaRecorrida, Periodicidad.DIARIA);
    }
    @Override
    public String toString(){
        return descripcion;
    }

}
