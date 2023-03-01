package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.json.JSONArray;
import org.json.JSONObject;


// fijense como correspondemos esto a persistencia (cada batch medicion tiene muchos mediciones)
@Entity
@Table
@NamedQuery(name = "getAllBatchMedicion", query = "select a from BatchMedicion a")
public class BatchMedicion {
    @Id
    @GeneratedValue
    private int id;
    // mappedBy = "medicion",
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Medicion> mediciones = new ArrayList<Medicion>();

    public BatchMedicion(List<Medicion> mediciones) {
        setMediciones(mediciones);
    }
    public BatchMedicion() {
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }
    public List<Medicion> getMediciones() {
        return this.mediciones;
    }
    
    @Override
    public String toString(){
        JSONObject json = new JSONObject().put("id", id);
        JSONArray JSONmediciones = new JSONArray();
        mediciones.forEach((med -> JSONmediciones.put(new JSONObject(med.toString()))));
        json.put("mediciones", (JSONmediciones));

        return json.toString();
    }
}
