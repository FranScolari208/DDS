package dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.MiembroPorSectorRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.exceptions.MiembroNoPuedeUnirseAOrganizacionException;

import javax.persistence.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table
@NamedQuery(name = "getAllSector", query = "select a from Sector a")
@NamedQuery(query = "Select s from Sector s where s.organizacion = :id", name = "find sector id")
@NamedQuery(query = "Select s from Sector s where s.id = :id", name = "find sector por id")
public class Sector {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String nombre;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private Organizacion organizacion;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Actividad> actividades;
    @Transient
    private List<Miembro> miembros;
    @Transient
    private List<Miembro> miembrosNoAceptados;
    @OneToMany(mappedBy = "sector", cascade = CascadeType.REMOVE)
    private List<MiembroPorSector> miembroPorSectorLista;


    @Override
    public String toString(){
        JSONObject json = new JSONObject()
        .put("id", id)
        .put("nombre", nombre)
        .put("miembros", new JSONArray(miembroPorSectorLista.stream().map(
            (miembroxsect) -> new JSONObject(miembroxsect.getMiembro().toString())
        ).collect(Collectors.toList())));


        return json.toString();
    }

    public List<MiembroPorSector> getMiembroPorSectorLista() {
        return miembroPorSectorLista;
    }

    public void setMiembroPorSectorLista(List<MiembroPorSector> miembroPorSectorLista) {
        this.miembroPorSectorLista = miembroPorSectorLista;
    }

    public Sector() {
        miembroPorSectorLista = new ArrayList<>();
        miembrosNoAceptados = new ArrayList<>();
        miembros = new ArrayList<>();
        actividades = new ArrayList<>();
    }

    public Sector(String nombre, Organizacion organizacion) {
        this.nombre = nombre;
        this.organizacion = organizacion;
        this.miembroPorSectorLista = new ArrayList<>();
        this.miembrosNoAceptados = new ArrayList<>();
        this.miembros = new ArrayList<>();
        this.actividades = new ArrayList<>();
        organizacion.addSector(this);
    }

    // Para tests
    public Sector(String nombre, Organizacion organizacion, List<Actividad> actividades, List<Miembro> miembros,
            List<Miembro> miembrosNoAceptados) {
        this.nombre = nombre;
        this.organizacion = organizacion;
        this.actividades = actividades;
        this.miembros = miembros;
        this.miembrosNoAceptados = miembrosNoAceptados;
    }

    public String getNombre() {
        return nombre;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public List<Miembro> getMiembros() {
        return miembros;
    }

    public List<Miembro> getMiembrosNoAceptados() {
        return miembrosNoAceptados;
    }

    public void setMiembros(List<Miembro> miembros) {
        this.miembrosNoAceptados = miembros;
    }

    public void setMiembrosAceptados(List<Miembro> miembros) {
        this.miembros = miembros;
    }

    public void addMiembro(Miembro miembro) {
        /*MiembroPorSector mps = new MiembroPorSector();
        mps.setMiembro(miembro);
        mps.setSector(this);
        mps.setStatus(true);
        this.miembroPorSectorLista.add(mps);*/
        this.miembrosNoAceptados.add(miembro);
    }

    public void aceptarMiembro(Miembro miembro) {
        List<MiembroPorSector> miembroPorSectorLista = miembro.getMiembroPorSectorLista();

        for(int i =0; i < miembroPorSectorLista.size(); i++){
            MiembroPorSector miembroPorSector = miembroPorSectorLista.get(i);
            Sector sectorMiembroPorSector = miembroPorSector.getSector();
            Organizacion organizacionSectorMiembroPorSector = sectorMiembroPorSector.getOrganizacion();
            if(!(organizacionSectorMiembroPorSector.getRazon_social().equals(organizacion.getRazon_social()))){
                this.miembrosNoAceptados.remove(miembro);
                this.miembros.add(miembro);
                miembro.addSector(this);
                miembro.setAceptado(true);
            }else{
                throw new MiembroNoPuedeUnirseAOrganizacionException(
                        "El miembro ya pertenece a un sector de la organizacion");
            }
        }

        /*if (!(sectoresMiembro.stream().anyMatch(sector -> sector.getOrganizacion().equals(organizacion)))) {
            this.miembrosNoAceptados.remove(miembro);
            this.miembros.add(miembro);
            miembro.addSector(this);
            miembro.setAceptado(true);
        } else {
            throw new MiembroNoPuedeUnirseAOrganizacionException(
                    "El miembro ya pertenece a un sector de la organizacion");
        }*/

    }

    public void addActividad(Actividad actividad) {
        this.actividades.add(actividad);
    }

    // calculo de los HC de cada miembro
    public Float calcularHC() {

        return (float) miembroPorSectorLista.stream()
                .mapToDouble(miembro -> miembro.calcularHCTrayectoCompartido(organizacion))
                .sum();

    }

    public Float visualizadorPorSector() {
        Float HUOrganizacion = organizacion.obtenerHU(organizacion.getMedibles());
        return this.calcularHC() * 100 / getMiembros().stream().count();
    }

    public boolean perteneceA(List<Sector> sectores) {
       return  sectores.contains(this);
    }

}
