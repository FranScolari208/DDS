package dds2022.grupo1.HuellaDeCarbono.entidades.Sector;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MUNI")
public class Municipio extends SectorTerritorial {
    // @Column
    // public int id;
    @OneToMany(mappedBy = "municipio", cascade = CascadeType.REMOVE)
    private List<Organizacion> organizaciones = new ArrayList<>();

    public Municipio(String nombre, List<Organizacion> organizaciones) {
        super(nombre);
        this.organizaciones = organizaciones;
    }

    public Municipio(String nombre) {
        super(nombre);
    }

    public Municipio() {
    }

    public List<Organizacion> getOrganizaciones() {
        return organizaciones;
    }

    public void setOrganizaciones(List<Organizacion> organizaciones) {
        this.organizaciones = organizaciones;
    }

    @Override
    public float calcularHC() {
        List<Float> hcOrganizaciones = new ArrayList<>();

        for (Organizacion organizacion : organizaciones) {
            Float huOrganizacion = organizacion.obtenerHU(organizacion.getMedibles());
            hcOrganizaciones.add(huOrganizacion);
        }
        return (float) hcOrganizaciones.stream().mapToDouble(Double::valueOf).sum();
    }
}
