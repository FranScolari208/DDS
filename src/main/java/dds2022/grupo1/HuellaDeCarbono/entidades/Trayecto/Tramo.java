package dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;

import javax.persistence.*;

import org.json.JSONObject;

@Entity
@Table
@NamedQuery(name = "getAllTramo", query = "select a from Tramo a")
public class Tramo extends Persistente {
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ubicacionActual", referencedColumnName = "id")
    private Ubicacion ubicacionActual;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ubicacionAnterior", referencedColumnName = "id", nullable = true)
    private Ubicacion ubicacionAnterior;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ubicacionProxima", referencedColumnName = "id")
    private Ubicacion ubicacionProxima;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "trayecto_id", referencedColumnName = "id")
    private Trayecto trayecto;

    public Ubicacion getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(Ubicacion ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public Ubicacion getUbicacionAnterior() {
        return ubicacionAnterior;
    }

    public void setUbicacionAnterior(Ubicacion ubicacionAnterior) {
        this.ubicacionAnterior = ubicacionAnterior;
    }

    public Ubicacion getUbicacionProxima() {
        return ubicacionProxima;
    }

    public void setUbicacionProxima(Ubicacion ubicacionProxima) {
        this.ubicacionProxima = ubicacionProxima;
    }

    public Tramo(Ubicacion ubicacionActual, Ubicacion ubicacionAnterior, Ubicacion ubicacionProxima) {
        this.ubicacionActual = ubicacionActual;
        this.ubicacionAnterior = ubicacionAnterior;
        this.ubicacionProxima = ubicacionProxima;
    }

    public Tramo(Ubicacion ubicacionActual, Ubicacion ubicacionAnterior, Ubicacion ubicacionProxima,
            Trayecto trayecto) {
        this.ubicacionActual = ubicacionActual;
        this.ubicacionAnterior = ubicacionAnterior;
        this.ubicacionProxima = ubicacionProxima;
        this.trayecto = trayecto;
    }

    // Primer tramo del recorrido no tiene ubicacion anterior por ende deberia haber
    // un constructor q tenga ubicacionAnterior en null

    public Tramo() {
    }

    public Tramo(Ubicacion ubicacionActual, Ubicacion ubicacionProxima) {
        this.ubicacionActual = ubicacionActual;
        this.ubicacionProxima = ubicacionProxima;
    }

    public Trayecto getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(Trayecto trayecto) {
        this.trayecto = trayecto;
    }

    public float calcularDistanciaTramo() {

        return (float) Math.sqrt(
                (ubicacionActual.getLongitud() - ubicacionProxima.getLongitud())
                        * (ubicacionActual.getLongitud() - ubicacionProxima.getLongitud())
                        + (ubicacionActual.getLatitud() - ubicacionProxima.getLatitud())
                                * (ubicacionActual.getLatitud() - ubicacionProxima.getLatitud()));

    };
    // private Ubicacion ubicacionActual;
    // @OneToOne(cascade = CascadeType.REMOVE)
    // @JoinColumn(name = "ubicacionAnterior", referencedColumnName = "id", nullable = true)
    // private Ubicacion ubicacionAnterior;
    // @OneToOne(cascade = CascadeType.REMOVE)
    // @JoinColumn(name = "ubicacionProxima", referencedColumnName = "id")
    // private Ubicacion ubicacionProxima;
    // @ManyToOne(cascade = CascadeType.REMOVE)
    // @JoinColumn(name = "trayecto_id", referencedColumnName = "id")
    // private Trayecto trayecto;
    @Override
    public String toString() {
        JSONObject json = new JSONObject()
        .put("id", id)
        .put("ubicacionActual", ubicacionActual.toString())
        .put("ubicacionActual", ubicacionActual.toString())
        .put("trayecto", trayecto.toString());
        
        if (ubicacionAnterior != null){
            json.put("ubicacionAnterior", ubicacionAnterior.toString());
        }
        return json.toString();
    }

}
