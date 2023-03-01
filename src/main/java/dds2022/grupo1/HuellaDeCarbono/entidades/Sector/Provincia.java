package dds2022.grupo1.HuellaDeCarbono.entidades.Sector;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@DiscriminatorValue("PROV")
public class Provincia extends SectorTerritorial{

    @Transient
    private List<Municipio> municipios = new ArrayList<>();

    public Provincia(String nombre, List<Municipio> municipios) {
        super(nombre);
        this.municipios = municipios;
    }

    public Provincia(String nombre) {
        super(nombre);
    }

    public Provincia() {
    }

    public List<Municipio> getDepartamentos() {
        return municipios;
    }

    public void setDepartamentos(List<Municipio> municipios) {
        this.municipios = municipios;
    }

   /* public int getId() {
        return id;
    }*/

    @Override
    public float calcularHC() {
        List<Float> hcDepartamentos = new ArrayList<>();

        for(Municipio municipio : municipios){
            Float huDepartamento = municipio.calcularHC();
            hcDepartamentos.add(huDepartamento);
        }
        return (float) hcDepartamentos.stream().mapToDouble(Double::valueOf).sum();
    }
}
