package dds2022.grupo1.HuellaDeCarbono.DTOs;

import org.json.JSONObject;

public class MedicionDTO {

    private int id = 0;
    private JSONObject actividad;
    private JSONObject consumo;
    private String periodoImputacion;

    public MedicionDTO(){
    }

    public MedicionDTO(int identificador, JSONObject act, JSONObject cons, String periodoImput) {
        id = identificador;
        actividad = act;
        consumo = cons;
        periodoImputacion = periodoImput;
    }

    public MedicionDTO(JSONObject act, JSONObject cons, String periodoImput) {
        actividad = act;
        consumo = cons;
        periodoImputacion = periodoImput;
    }

    public int getId() { return id; }

    public JSONObject getActividad() { return actividad; }

    public JSONObject getConsumo() { return consumo; }

    public String getPeriodoImputacion() { return periodoImputacion; }
}
