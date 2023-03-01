package dds2022.grupo1.HuellaDeCarbono.DTOs;

import org.json.JSONObject;

public class OrganizacionDTO {

    private int id = 0;
    private String razonSocial;
    private String imagen;
    private JSONObject ubicacion;
    private String clasificacion;
    private String tipoOrganizacion;

    public OrganizacionDTO() {
    }

    public OrganizacionDTO(int identificador, String nombre, JSONObject ubi, String clasif, String tipo) {
        id = identificador;
        razonSocial = nombre;
        ubicacion = ubi;
        clasificacion = clasif;
        tipoOrganizacion = tipo;
    }

    public OrganizacionDTO(int identificador, String imagenDTO, String nombre, JSONObject ubi, String clasif, String tipo) {
        id = identificador;
        razonSocial = nombre;
        imagen = imagenDTO;
        ubicacion = ubi;
        clasificacion = clasif;
        tipoOrganizacion = tipo;
    }

    public OrganizacionDTO(String nombre, JSONObject ubi, String clasif, String tipo) {
        razonSocial = nombre;
        ubicacion = ubi;
        clasificacion = clasif;
        tipoOrganizacion = tipo;
    }

    public OrganizacionDTO(String nombre, String imagenDTO, JSONObject ubi, String clasif, String tipo) {
        razonSocial = nombre;
        imagen = imagenDTO;
        ubicacion = ubi;
        clasificacion = clasif;
        tipoOrganizacion = tipo;
    }

    public int getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public JSONObject getUbicacion() {
        return ubicacion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public String getTipoOrganizacion() {
        return tipoOrganizacion;
    }

}
