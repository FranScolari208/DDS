package dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.MiembroRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TrayectoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.enums.TipoDni;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.json.JSONObject;

import static dds2022.grupo1.HuellaDeCarbono.services.utils.miembro_utils.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"dni"}) })
@NamedQuery(name = "getAllMiembro", query = "select a from Miembro a")
@NamedQuery(name = "find miembro by id", query = "select a from Miembro a where a.id = :id")
public class Miembro {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String nombre;
    @Column
    private String apellido;
    @Enumerated(EnumType.STRING)
    private TipoDni tipoDni;
    @Column
    private Long dni;
    @Transient
    private List<Sector> sectores = new ArrayList<>();

    @OneToMany(mappedBy = "miembro", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<MiembroPorSector> miembroPorSectorLista;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "trayectoPorMiembro")
    private List<Trayecto> trayectos = new ArrayList<>();
    @Transient // despues borrar y arreglar todo esto
    private boolean aceptado;

    @Override
    public String toString() {
        return new JSONObject()
        .put("id", id)
        .put("nombre", nombre)
        .put("apellido", apellido)
        .put("tipo_dni", tipoDni)
        .put("dni", dni)
        .toString();
    }

    public Miembro() {
        miembroPorSectorLista = new ArrayList<>();
    }

    public Miembro(String nombre, String apellido, TipoDni tipoDni, Long dni, List<Trayecto> trayectos) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDni = tipoDni;
        this.dni = dni;
        this.trayectos = trayectos;

    }

    public Miembro(String nombre, String apellido, TipoDni tipoDni, Long dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDni = tipoDni;
        this.dni = dni;
    }

    public List<MiembroPorSector> getMiembroPorSectorLista() {
        return miembroPorSectorLista;
    }

    public void setMiembroPorSectorLista(List<MiembroPorSector> miembroPorSectorLista) {
        this.miembroPorSectorLista = miembroPorSectorLista;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoDni getTipoDni() {
        return tipoDni;
    }

    public void setTipoDni(TipoDni tipoDni) {
        this.tipoDni = tipoDni;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public List<Sector> getSectores() {
        return sectores;
    }

    public void addSector(Sector sector) {
        sectores.add(sector);
    }

    public void setSectores(List<Sector> sectores) {
        this.sectores = sectores;
    }

    public List<Trayecto> getTrayectos() {
        return TrayectoRepositorio.getInstancia().getTrayectosDelMiembro(this.id);
    }

    public List<PeriodoDeImputacion> getPeriodosDeImputacionDeTrayectos(Organizacion org) {

        // System.out.println(getTrayectosOrganizacion(org).stream().map(trayecto ->
        // trayecto.getPeriodoDeImputacion()).collect(Collectors.toList()));
        // System.out.println(getTrayectosOrganizacion(org));
        // System.out.println(getTrayectosOrganizacion(org).get(0));
        // System.out.println(getTrayectosOrganizacion(org).get(0).getPeriodoDeImputacion());
        List<PeriodoDeImputacion> periodos = getTrayectosOrganizacion(org).stream()
                .map(trayecto -> trayecto.getPeriodoDeImputacion()).collect(Collectors.toList());
        List<PeriodoDeImputacion> periodosVistos = new ArrayList<>();
        for (PeriodoDeImputacion periodo : periodos) {
            if (!periodosVistos.stream().map(
                    PeriodoDeImputacion::toString).collect(Collectors.toList())
                    .contains(periodo.toString())) {
                periodosVistos.add(periodo);
            }
        }
        return periodosVistos;
    }

    // tendra que acceder por el repo en vez desde la lsita del miembro?
    public List<Trayecto> getTrayectosOrganizacion(Organizacion org) {
        return filterTrayectosOrganizacion(trayectos, org);
    }
    public List<Trayecto> getTrayectosOrganizacion(Organizacion org, Miembro miembro) {
        List<Trayecto> trayectosMiembro = MiembroRepositorio.getInstancia().getTrayectosDelMiembro(miembro);
        return filterTrayectosOrganizacion(trayectosMiembro, org);
    }

    public List<Trayecto> getTrayectosPeriodo(PeriodoDeImputacion periodo) {
        return filterTrayectosPeriodo(trayectos, periodo);
    }

    public List<Trayecto> getTrayectosAnio(PeriodoDeImputacion periodo) {
        return filterTrayectoPorsAnioPeriodo(trayectos, periodo);
    }

    public List<Trayecto> getTrayectosPeriodoOrganizacion(PeriodoDeImputacion periodo, Organizacion org) {
        return filterTrayectosOrganizacion(
                filterTrayectosPeriodo(trayectos, periodo),
                org);
    }

    public void setTrayectos(List<Trayecto> trayectos) {
        this.trayectos = trayectos;
    }

    public void addTraycto(Trayecto trayecto) {
        this.trayectos.add(trayecto);
    }

    // no necesariamente por ser el miembro el que inicia el caso de uso
    // significa que deba residir en el el metodo
    // (capaz podria estar en el sector y que tome de param a un Miembro)
    public void vincularASector(Sector sector) {
        sector.addMiembro(this);
        //sectores.add(sector);

    }

    public void addTrayecto(Trayecto trayecto) {
        trayectos.add(trayecto);
    }

    public float calcularHCtotalMiembro(Organizacion organizacion, Miembro miembro) throws NoExisteException {
        List<Trayecto> trayectosFiltrados = this.getTrayectosOrganizacion(organizacion, miembro);

        return (float) trayectosFiltrados.stream().mapToDouble(Trayecto::calcularHCDivididoParticipantes).sum();
    }

    // TODO: diferenciar para que organizacion va cada HC

    public float calcularMiImpacto(Organizacion organizacion) {
        Float HUOrganizacion = organizacion.obtenerHU(organizacion.getMedibles());
        return this.calcularHCTrayectoCompartido(organizacion) * 100 / HUOrganizacion;
    }

    public float calcularHCTrayectoCompartido(Organizacion organizacion) throws NoExisteException {
        return (float) getTrayectosOrganizacion(organizacion).stream()
                .mapToDouble(Trayecto::calcularHCDivididoParticipantes).sum();
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public List<Trayecto> getTrayectosDelMiembro() {
        return MiembroRepositorio.getInstancia().getTrayectosDelMiembro(this);
    }

    public void addMiembroPorSector(MiembroPorSector mps) {
        miembroPorSectorLista.add(mps);
    }
}
