package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Periodicidad;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("transportePublico")
public class TransportePublico extends MedioDeTransporte {
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "tipoTransporte_id", referencedColumnName = "id")
    private TipoTransporte tipoTransporte;
    @OneToMany(mappedBy = "transportePublico", cascade = CascadeType.REMOVE)
    private List<Parada> paradas = new ArrayList<>();
    @Column
    private String linea;

    public TransportePublico() {
    }

    public TransportePublico(TipoTransporte tipoTransporte, List<Parada> estaciones, String nomLinea,
            String descripcion) {
        super(descripcion);
        this.tipoTransporte = tipoTransporte;
        paradas = estaciones;
        linea = nomLinea;
    }

    public TipoTransporte getTipoTransporte() {
        return tipoTransporte;
    }

    public List<Parada> getParadas() {
        return paradas;
    }

    public String getLinea() {
        return linea;
    }

    public void setTipoTransporte(TipoTransporte tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public void setParadas(List<Parada> paradas) {
        this.paradas = paradas;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public TransportePublico(TipoTransporte tipoTransporte, List<Parada> estaciones, String nomLinea) {
        this.tipoTransporte = tipoTransporte;
        paradas = estaciones;
        linea = nomLinea;
    }

    @Override
    public float calcularDistancia(List<Tramo> tramos) {

        List<Ubicacion> ubicacionParadas = paradas.stream().map(Parada::getUbicacion).collect(Collectors.toList()); // obtiene
                                                                                                                    // la
        int comienzo = tramos.get(0).getUbicacionActual().indexIn(ubicacionParadas);
        int fin = tramos.get(tramos.size() - 1).getUbicacionProxima().indexIn(ubicacionParadas);

        List<Parada> paradasViajadas = paradas.subList(comienzo, fin);

        return (float) paradasViajadas.stream().mapToDouble(Parada::getDistanciaParadaSiguiente).sum();
    }

    @Override
    public Consumo calcularConsumo(float distanciaRecorrida) {
        TipoConsumo tipoConsumo = TipoConsumoRepositorio.getInstancia().getTipoConsumo("Distancia media recorrida");
        return new Consumo(tipoConsumo, distanciaRecorrida, Periodicidad.DIARIA);
    }

    @Override
    public String toString() {
        return "\n{ Linea: " + this.linea + "\nVehiculo: " + this.tipoTransporte + "\nCantidad de Paradas: "
                + String.valueOf(paradas.size()) + " }";
    }

}