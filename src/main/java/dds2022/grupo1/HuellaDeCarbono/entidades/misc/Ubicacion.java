package dds2022.grupo1.HuellaDeCarbono.entidades.misc;

import javax.persistence.*;

import org.json.JSONObject;

import java.util.List;

@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "latitud", "longitud" }) })
@NamedQuery(name = "getAllUbicacion", query = "select a from Ubicacion a")
@Entity
public class Ubicacion {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String direccion = "";
    @Column
    private String altura = "";
    @Column
    private Float latitud;
    @Column
    private Float longitud;
    @Column
    private String localidad;
    @Column
    private String provincia;
    @Column
    private String municipio;

    public Ubicacion() {
    }

    public Ubicacion(String direccion, Float latitud, Float longitud) {
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        altura = "";
        direccion = "";
    }

    public Ubicacion(Float latitud, Float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
        altura = "";
        direccion = "";
    }

    public Ubicacion(String direccion, String altura, Float latitud, Float longitud, String localidad) {
        this.direccion = direccion;
        this.altura = altura;
        this.latitud = latitud;
        this.longitud = longitud;
        this.localidad = localidad;
    }

    public Ubicacion(String direccion, String altura, Float latitud, Float longitud, String localidad, String provincia,
            String municipio) {
        this.direccion = direccion;
        this.altura = altura;
        this.latitud = latitud;
        this.longitud = longitud;
        this.localidad = localidad;
        this.provincia = provincia;
        this.municipio = municipio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public boolean esMismaUbicacion(Ubicacion ubicacion) {
        return Float.compare(ubicacion.getLatitud(), latitud) == 0
                & Float.compare(ubicacion.getLongitud(), longitud) == 0;
    }

    public int indexIn(List<Ubicacion> ubicaciones) {
        int index = 0;
        for (Ubicacion ubi : ubicaciones) {
            if (this.esMismaUbicacion(ubi)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public String toString(){
        String jsonString = new JSONObject()
            .put("id", id)
            .put("direccion", direccion)
            .put("altura", altura)
            .put("latitud", latitud)
            .put("longitud", longitud)
            .put("localidad", localidad)
            .put("provincia", provincia)
            .put("municipio", municipio)
            .toString();
        return jsonString;
    }

}
