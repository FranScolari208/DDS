package dds2022.grupo1.HuellaDeCarbono.DTOs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;

public class TrayectoDTO {

    /*
     * 
     * formato:
     * trayecto: {
     * "tramos": [
     * {
     * "actual":{
     * "direccion": String,
     * "latitud": float,
     * "longitud": float
     * },
     * "siguiente":{
     * "direccion": String,
     * "latitud": float,
     * "longitud": float
     * },
     * OPCIONAL -> "anterior":{
     * "direccion": String,
     * "latitud": float,
     * "longitud": float
     * },
     * },
     * {}, ...
     * ]
     * }
     * 
     * 
     * 
     */

    private int id;
    private JSONObject ubicacionSalida;
    private JSONObject ubicacionLlegada;
    private JSONArray medioDeTransporte;
    private String periodoImputacion;
    private JSONArray tramos;
    private int cantidadParticipantes;
    private JSONArray dniMiembros;

    public TrayectoDTO(
            JSONObject ubiSalida, JSONObject ubiLlegada,
            JSONArray transporte, String imputacion,
            JSONArray tramos, int participantes,
            JSONArray miembrosDnis) {
        ubicacionSalida = ubiSalida;
        ubicacionLlegada = ubiLlegada;
        medioDeTransporte = transporte;
        periodoImputacion = imputacion;
        this.tramos = tramos;
        cantidadParticipantes = participantes;
        dniMiembros = miembrosDnis;
    }

    public TrayectoDTO(
            int identificador,
            JSONObject ubiSalida, JSONObject ubiLlegada,
            JSONArray transporte, String imputacion,
            JSONArray tramos, int participantes,
            JSONArray miembrosDnis) {
        id = identificador;
        ubicacionSalida = ubiSalida;
        ubicacionLlegada = ubiLlegada;
        medioDeTransporte = transporte;
        periodoImputacion = imputacion;
        this.tramos = tramos;
        cantidadParticipantes = participantes;
        dniMiembros = miembrosDnis;
    }

    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return JSONObject return the ubicacionSalida
     */
    public Ubicacion getUbicacionSalida() {
        return getUbicacion(ubicacionSalida);
    }

    public Ubicacion getUbicacion(JSONObject ubi) {
        return new Ubicacion(ubi.getString("direccion"),
                ubi.getFloat("latitud"),
                ubi.getFloat("longitud"));
    }

    /**
     * @param ubicacionSalida the ubicacionSalida to set
     */
    public void setUbicacionSalida(JSONObject ubicacionSalida) {
        this.ubicacionSalida = ubicacionSalida;
    }

    /**
     * @return JSONObject return the ubicacionLlegada
     */
    public Ubicacion getUbicacionLlegada() {
        return getUbicacion(ubicacionLlegada);

    }

    /**
     * @param ubicacionLlegada the ubicacionLlegada to set
     */
    public void setUbicacionLlegada(JSONObject ubicacionLlegada) {
        this.ubicacionLlegada = ubicacionLlegada;
    }

    /**
     * @return String return the medioDeTransporte
     */
    public List<String> getMedioDeTransporte() {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < medioDeTransporte.length(); i++) {
            temp.add(medioDeTransporte.getString(i));
        }
        return temp;
    }

    /**
     * @param medioDeTransporte the medioDeTransporte to set
     */
    public void setMedioDeTransporte(JSONArray medioDeTransporte) {
        this.medioDeTransporte = medioDeTransporte;
    }

    /**
     * @return String return the periodoImputacion
     */
    public String getPeriodoImputacion() {
        return periodoImputacion;
    }

    /**
     * @param periodoImputacion the periodoImputacion to set
     */
    public void setPeriodoImputacion(String periodoImputacion) {
        this.periodoImputacion = periodoImputacion;
    }

    /**
     * @return JSONArray return the tramos
     */
    public List<Tramo> getTramos() {
        List<Tramo> tramosTemp = new ArrayList<>();
        for (int i = 0; i < tramos.length(); i++) {
            JSONObject infoTramo = tramos.getJSONObject(i);
            // en este array hay ubi anterior, actual y siguiente (si no hay clave de
            // anterior es xq es la primera)
            Tramo temp;
            Ubicacion actual = getUbicacion(infoTramo.getJSONObject("actual"));
            Ubicacion siguiente = getUbicacion(infoTramo.getJSONObject("siguiente"));
            try {
                Ubicacion anterior = getUbicacion(infoTramo.getJSONObject("anterior"));
                temp = new Tramo(actual, anterior, siguiente);
            } catch (JSONException e) {
                temp = new Tramo(actual, siguiente);
            }
            tramosTemp.add(temp);
        }
        return tramosTemp;
    }

    /**
     * @param tramos the tramos to set
     */
    public void setTramos(JSONArray tramos) {
        this.tramos = tramos;
    }

    /**
     * @return int return the cantidadParticipantes
     */
    public int getCantidadParticipantes() {
        return cantidadParticipantes;
    }

    /**
     * @param cantidadParticipantes the cantidadParticipantes to set
     */
    public void setCantidadParticipantes(int cantidadParticipantes) {
        this.cantidadParticipantes = cantidadParticipantes;
    }

    /**
     * @return JSONArray return the dniMiembros
     */
    public JSONArray getDniMiembros() {
        return dniMiembros;
    }

    /**
     * @param dniMiembros the dniMiembros to set
     */
    public void setDniMiembros(JSONArray dniMiembros) {
        this.dniMiembros = dniMiembros;
    }

}
