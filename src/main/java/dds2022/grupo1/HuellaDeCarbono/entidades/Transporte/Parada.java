package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;

import javax.persistence.*;

@Entity
@Table
@NamedQuery(name = "getAllParada", query = "select a from Parada a")
public class Parada extends Persistente {
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ubicacionParado", referencedColumnName = "id")
    private Ubicacion ubicacion;
    @Column
    private Float distanciaParadaAnterior;
    @Column
    private Float distanciaParadaSiguiente;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "transporte_id", referencedColumnName = "id")
    private TransportePublico transportePublico;

    public Parada(Ubicacion parada, Float distanciaAnt, Float distanciaSig) {
        ubicacion = parada;
        distanciaParadaAnterior = distanciaAnt;
        distanciaParadaSiguiente = distanciaSig;
    }

    public Parada(Ubicacion ubicacion, Float distanciaParadaAnterior, Float distanciaParadaSiguiente,
            TransportePublico transportePublico) {
        this.ubicacion = ubicacion;
        this.distanciaParadaAnterior = distanciaParadaAnterior;
        this.distanciaParadaSiguiente = distanciaParadaSiguiente;
        this.transportePublico = transportePublico;
    }

    public Parada() {
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Float getDistanciaParadaAnterior() {
        return distanciaParadaAnterior;
    }

    public void setDistanciaParadaAnterior(Float distanciaParadaAnterior) {
        this.distanciaParadaAnterior = distanciaParadaAnterior;
    }

    public Float getDistanciaParadaSiguiente() {
        return distanciaParadaSiguiente;
    }

    public void setDistanciaParadaSiguiente(Float distanciaParadaSiguiente) {
        this.distanciaParadaSiguiente = distanciaParadaSiguiente;
    }

    public TransportePublico getTransportePublico() {
        return transportePublico;
    }

    public void setTransportePublico(TransportePublico transportePublico) {
        this.transportePublico = transportePublico;
    }

}
