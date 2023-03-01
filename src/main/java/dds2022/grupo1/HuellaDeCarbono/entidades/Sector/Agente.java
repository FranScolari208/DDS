package dds2022.grupo1.HuellaDeCarbono.entidades.Sector;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoImplementadoException;

import javax.persistence.*;

import org.json.JSONObject;

@Table
@NamedQuery(name = "getAllAgente", query = "select a from Agente a")
@Entity
public class Agente extends Persistente {
    @Column
    private String nombre;
    @Column
    private String email;
    @Column
    private String telefono;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "sectorTerritorial_id", referencedColumnName = "id", nullable = true)
    private SectorTerritorial sector;

    @Override
    public String toString() {
        JSONObject json = new JSONObject()
        .put("id", id)
        .put("nombre", nombre)
        .put("email", email)
        .put("telefono", telefono);

        if(sector != null) json.put("sector", sector.toString());
        return json.toString();
    }

    public Agente(String nombre, String email, String telefono, SectorTerritorial sector) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.sector = sector;
    }

    public Agente(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public Agente() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public SectorTerritorial getSector() {
        return sector;
    }

    public void setSector(SectorTerritorial sector) {
        this.sector = sector;
    }

    float calcularHCSector() throws NoImplementadoException {
        return sector.calcularHC();
    }
}
