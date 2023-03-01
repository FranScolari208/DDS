package dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.FachadaOrg;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.MedioDeNotificacion;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.MedibleRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.SectorTerritorial;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.*;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.ParserFactory;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.enums.Clasificacion;
import dds2022.grupo1.HuellaDeCarbono.services.LogCalculos;
import javax.persistence.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table //(uniqueConstraints = { @UniqueConstraint(columnNames = { "razon_social" }) })
@NamedQuery(name = "getAllOrganizacion", query = "select o from Organizacion o")
@NamedQuery(name= "getOrganizacion", query = "select o from Organizacion o where o.id = :id")
public class Organizacion implements FachadaOrg {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String razon_social;
    @Column
    private String imagen;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion ubicacion;

    @OneToMany(mappedBy = "organizacion", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Sector> sectores = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Clasificacion clasificacion;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Medible> medibles = new ArrayList<>();

    @OneToOne()
    @JoinColumn(name = "tipoOrg_id", referencedColumnName = "id")
    private TipoOrganizacion tipoOrganizacion;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "municipio_id", referencedColumnName = "id")
    private SectorTerritorial municipio;

    @Transient
    private MedioDeNotificacion medio;

    @OneToMany(mappedBy = "organizacion", cascade = CascadeType.REMOVE)
    private List<LogCalculos> logCalculos = new ArrayList<>();


    public Organizacion() {
    }

    public Organizacion(String razon_social, TipoOrganizacion tipoOrganizacion, Ubicacion ubicacion,
            Clasificacion clasificacion) {
        this.sectores = new ArrayList<>();
        this.razon_social = razon_social;
        this.tipoOrganizacion = tipoOrganizacion;
        this.ubicacion = ubicacion;
        this.clasificacion = clasificacion;
    }
    public Organizacion(String razon_social, String imagen, TipoOrganizacion tipoOrganizacion, Ubicacion ubicacion,
                        Clasificacion clasificacion) {
        this.sectores = new ArrayList<>();
        this.imagen = imagen;
        this.razon_social = razon_social;
        this.tipoOrganizacion = tipoOrganizacion;
        this.ubicacion = ubicacion;
        this.clasificacion = clasificacion;
    }

    public Organizacion(String razon_social, Ubicacion ubicacion, List<Sector> sectores, Clasificacion clasificacion,
                        List<Medible> medibles, TipoOrganizacion tipoOrganizacion, MedioDeNotificacion medio) {
        this.razon_social = razon_social;
        this.ubicacion = ubicacion;
        this.sectores = sectores;
        this.clasificacion = clasificacion;
        this.medibles = medibles;
        this.tipoOrganizacion = tipoOrganizacion;
        this.medio = medio;
    }

    public Organizacion(String razon_social, Ubicacion ubicacion, List<Sector> sectores, Clasificacion clasificacion,
            List<Medible> medibles, TipoOrganizacion tipoOrganizacion) {
        this.razon_social = razon_social;
        this.ubicacion = ubicacion;
        this.sectores = sectores;
        this.clasificacion = clasificacion;
        this.medibles = medibles;
        this.tipoOrganizacion = tipoOrganizacion;
    }
    public Organizacion(String razon_social, Ubicacion ubicacion, List<Sector> sectores, Clasificacion clasificacion,
                        List<Medible> medibles, TipoOrganizacion tipoOrganizacion, String imagen) {
        this.razon_social = razon_social;
        this.ubicacion = ubicacion;
        this.sectores = sectores;
        this.clasificacion = clasificacion;
        this.medibles = medibles;
        this.tipoOrganizacion = tipoOrganizacion;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public TipoOrganizacion getTipoOrganizacion() {
        return tipoOrganizacion;
    }

    public void setTipoOrganizacion(TipoOrganizacion tipoOrganizacion) {
        this.tipoOrganizacion = tipoOrganizacion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Sector> getSectores() {
        return sectores;
    }

    public void setSectores(List<Sector> sectores) {
        this.sectores = sectores;
    }

    public void addSector(Sector sector) {
        List<Sector> sectores = getSectores();
        sectores.add(sector);
        setSectores(sectores);
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public List<Medible> getMedibles() {        
        return MedibleRepositorio.getInstancia().findSectorWithId(this);
    }

    public void setMedibles(List<Medible> medibles) {
        this.medibles = medibles;
    }

    public void addMedible(Medible medible) {
        this.medibles.add(medible);
    }

    public void setMunicipio(SectorTerritorial municipio) {
        this.municipio = municipio;
    }

    public SectorTerritorial getMunicipio() {
        return municipio;
    }

    // las org deben cargar en el sist un archivo de CSV
    public void cargarMedicion(String nombreMedicionArchivo) throws Exception {
        ParserMedicion parser = ParserFactory.crearParserMedicion(nombreMedicionArchivo);
        parser.leerMediciones().forEach(this::addMedible);

    }

    void darAltaMiembro(Miembro miembro, Sector sector) {
        sector.aceptarMiembro(miembro);
    }

    public void cargarParametrosDeArchivo(String nombreArchivo) throws Exception {
        ParserParametrosSistema parserParams = ParserFactory.crearParserParametrosSistema(nombreArchivo);
        cargarParametros(parserParams.leerParametrosSistema());
    }

    @Override
    public void cargarParametros(Map<String, Float> parametrosSistema) {
        // asumimos que el string es el nombre del tipo de consumo
        // y la unidad del consumo separados por coma.
        // el float el FE(valor)
        TipoConsumoRepositorio repo = TipoConsumoRepositorio.getInstancia();
        for (Map.Entry<String, Float> param : parametrosSistema.entrySet()) {
            List<String> consumoYunidad = Arrays.asList(param.getKey().split(","));

            String nombre = consumoYunidad.get(0);
            Unidad unidad = new Unidad(consumoYunidad.get(1).toUpperCase());

            FactorEmision factorEmision = new FactorEmision(param.getValue());

            /*
             * Float valorFE = param.getValue();
             * FactorEmision factorEmision = new FactorEmision(valorFe,
             * unidadCorrespondiente)
             * 
             * repo.addTipoConsumo(new TipoConsumo(unidad, factorEmision, nombre));
             * 
             */
            repo.save(new TipoConsumo(unidad, factorEmision, nombre));
        }
    }

    public Float calcularHCFecha(PeriodoDeImputacion fecha, Collection<Medible> mediciones) {

        List<Medible> medicionesfiltradas = new ArrayList<>();
        medicionesfiltradas = mediciones.stream()
                .filter(medicion -> medicion.getPeriodoImputacion().esMismoPeriodo(fecha)).collect(Collectors.toList());
        return this.obtenerHU(medicionesfiltradas);
    }

    public Float calcularHCTracyectosFecha(PeriodoDeImputacion fecha, Collection<Trayecto> trayectos) {
        List<Trayecto> trayectosFiltrados = new ArrayList<>();
        trayectosFiltrados = trayectos.stream()
                .filter(trayecto -> trayecto.getPeriodoDeImputacion().esMismoPeriodo(fecha))
                .collect(Collectors.toList());
        return (float) trayectosFiltrados.stream().mapToDouble(Trayecto::calcularHC).sum();
    }

    public Float calcularHCFechaAnio(int anio, Collection<Medible> mediciones) {
        PeriodoDeImputacion periodoDeImputacion = new PeriodoDeImputacion(anio);
        List<Medible> medicionesfiltradas = new ArrayList<>();
        medicionesfiltradas = mediciones.stream()
                .filter(medicion -> medicion.getPeriodoImputacion().esMismoAnio(periodoDeImputacion))
                .collect(Collectors.toList());
        return this.obtenerHU(medicionesfiltradas);
    }

    public Float calcularHCTracyectosAnio(int anio, Collection<Trayecto> trayectos) {
        List<Trayecto> trayectosFiltrados = new ArrayList<>();
        PeriodoDeImputacion periodoDeImputacion = new PeriodoDeImputacion(anio);
        trayectosFiltrados = trayectos.stream()
                .filter(trayecto -> trayecto.getPeriodoDeImputacion().esMismoAnio(periodoDeImputacion))
                .collect(Collectors.toList());
        return (float) trayectosFiltrados.stream().mapToDouble(Trayecto::calcularHC).sum();
    }

    @Override
    public Float obtenerHU(Collection<Medible> mediciones) {
        Float total = 0f;
        for (Medible medible : mediciones) {
            total += medible.getConsumo().calcularHC();
        }

        // total += (float) sectores.stream().mapToDouble(Sector::calcularHC).sum();
        return total;
    }

    public Float obtenerHUXActividad(Collection<Medible> mediciones, List<String> nombres) {
        Float total = 0f;

        for (Medible medible : mediciones) {
            total += medible.getConsumo().calcularHC();
            if(!nombres.contains(medible.getActividad().getNombre())){
                nombres.add(medible.getActividad().getNombre());
            }

        }

        return total;
    }



    public String obtenerUnidad(Collection<Medible> mediciones) {
        String unidad = "";
        for (Medible medible : mediciones) {
            unidad = medible.getConsumo().getTipoConsumo().convertidorUnidad().getNombre();// esto rompe porque nps
                                                                                           // cambia el valor del factor
                                                                                           // emision
        }
        return unidad;
    }

    public Float cuentaVisualizador() {

        return this.obtenerHU(this.medibles) / (sectores.stream().map(Sector::getMiembros).count());
    }

    @Override
    public String toString(){
        JSONObject json = new JSONObject()
                  .put("id", id)
                  .put("razonSocial", razon_social)
                  .put("ubicacion", new JSONObject(ubicacion.toString()))
                  .put("clasificacion", clasificacion.toString())
                  .put("tipoOrganizacion", tipoOrganizacion.toString())
                  .put("imagen", imagen);
            
        JSONArray sectoresArr = new JSONArray();
        for(Sector sect : sectores){
            sectoresArr.put(new JSONObject(sect.toString()));
        }
        json.put("sectores", sectoresArr);
                
                  //   .put("sectores", sectores.toString())
                //   .put("municipio", municipio.toString())
                  return json.toString();
       
    } 

}
