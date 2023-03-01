package dds2022.grupo1.HuellaDeCarbono.DTOs;

import org.json.JSONObject;

public class TramoDTO {
    private int id = 0;
    private JSONObject trayecto;
    private JSONObject ubicacionActual;
    private JSONObject ubicacionAnterior;
    private JSONObject ubicacionProxima;

    public TramoDTO(int id, JSONObject ubicacionActual, JSONObject ubicacionAnterior, JSONObject ubicacionProxima, JSONObject trayecto) {
        this.id = id;
        this.ubicacionActual = ubicacionActual;
        this.ubicacionAnterior = ubicacionAnterior;
        this.ubicacionProxima = ubicacionProxima;
        this.trayecto = trayecto;
    }
    public TramoDTO(int id, JSONObject ubicacionActual, JSONObject ubicacionProxima, JSONObject trayecto) {
        this.id = id;

        this.ubicacionActual = ubicacionActual;
        this.ubicacionProxima = ubicacionProxima;
        this.trayecto = trayecto;
    }


    public TramoDTO() {
    }
    public int getId() {
        return id;
    }


    public JSONObject getUbicacionActual() {
        return ubicacionActual;
    }

    public JSONObject getUbicacionAnterior() {
        return ubicacionAnterior;
    }

    public JSONObject getUbicacionProxima() {
        return ubicacionProxima;
    }

    public JSONObject getTrayecto() {
        return trayecto;
    }

}
