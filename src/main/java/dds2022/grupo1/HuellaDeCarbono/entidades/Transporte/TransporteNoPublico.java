package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Adaptador;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.enums.Periodicidad;
import dds2022.grupo1.HuellaDeCarbono.services.ServicioGeoref;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("transporteNoPublico")
@NamedQuery(name = "getAllTransporteNoPublico", query = "select a from TransporteNoPublico a")
@Inheritance
public class TransporteNoPublico extends MedioDeTransporte {
    @Transient
    Adaptador adaptador = new ServicioGeoref();
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "tipoTransporte_id", referencedColumnName = "id")
    private TipoTransporte tipoTransporte;

    public TransporteNoPublico(TipoTransporte transporte, String descripcion) {
        super(descripcion);
        tipoTransporte = transporte;
    }

    public TransporteNoPublico(TipoTransporte transporte) {
        tipoTransporte = transporte;
    }

    public TransporteNoPublico() {
    }

    @Override
    public float calcularDistancia(List<Tramo> tramos) {
        return (float) tramos.stream().mapToDouble(
                tramo -> adaptador.distancia(
                        tramo.getUbicacionActual(),
                        tramo.getUbicacionProxima()))
                .sum();
    }

    @Override
    public Consumo calcularConsumo(float distanciaRecorrida) {
        TipoConsumo tipoConsumo = TipoConsumoRepositorio.getInstancia().getTipoConsumo("Distancia media recorrida");
        return new Consumo(tipoConsumo, distanciaRecorrida, Periodicidad.DIARIA);
    }

    public String getNombre() {
        return tipoTransporte.toString();
    }

}
