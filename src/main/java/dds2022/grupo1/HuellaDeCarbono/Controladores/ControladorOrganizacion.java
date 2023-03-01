
package dds2022.grupo1.HuellaDeCarbono.Controladores;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.MiembroPorSector;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.TipoOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.SectorTerritorial;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Mes;
import dds2022.grupo1.HuellaDeCarbono.exceptions.MiembroNoPuedeUnirseAOrganizacionException;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.DTOFactory;
import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;

import org.json.JSONObject;

import db.EntityManagerHelper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class ControladorOrganizacion extends ControladorBase<Organizacion> {
    HashMap<String, Object> parametrosReporte = new HashMap<>();

    public void setRepo() {
        repo = OrganizacionRepositorio.getInstancia();
    }

    public ControladorOrganizacion() {
        super("orgs");
        post("/" + nombre, (request, response) -> {
            JSONObject body = new JSONObject(request.body());
            Organizacion org = DTOFactory.getOrganizacion(body);
       
            System.out.print(org.toString());
            Ubicacion ubiPersistida = UbicacionRepositorio.getInstancia().save(org.getUbicacion());
            org.setUbicacion(ubiPersistida);
            TipoOrganizacion tipoOrgPers = TipoOrganizacionRepositorio.getInstancia().save(org.getTipoOrganizacion());
            org.setTipoOrganizacion(tipoOrgPers);

            // WARNING: ACA
            if(org.getMunicipio() != null) {
                SectorTerritorial sectorPers = SectorTerritorialRepositorio.getInstancia().save(org.getMunicipio());
                org.setMunicipio(sectorPers);
            }

            org = repo.save(org);
            response.status(202);
            return org;
        }, json());

        put("/" + nombre + "/:pk", (request, response) -> {
            JSONObject body = new JSONObject(request.body());
            Long orgId = Long.parseLong(request.params(":pk"));

            if (!body.has("id")) {
                body.put("id", orgId);
            }
            // repo.get(orgId);
            // repo.delete(parsePK(request));
            Organizacion org = DTOFactory.getOrganizacion(body);
            org.setId(parsePK(request));
            System.out.print(org.toString());
            Ubicacion ubiPersistida = UbicacionRepositorio.getInstancia().save(org.getUbicacion());
            org.setUbicacion(ubiPersistida);
            TipoOrganizacion tipoOrgPers = TipoOrganizacionRepositorio.getInstancia().save(org.getTipoOrganizacion());
            org.setTipoOrganizacion(tipoOrgPers);

            if(org.getMunicipio() != null) {
                // WARNING: ACA
                SectorTerritorial sectorPers = SectorTerritorialRepositorio.getInstancia().save(org.getMunicipio());
                org.setMunicipio(sectorPers);
                //guiardar los objetos que lo componen tmb
            }

            //va a entrar en el merge
            repo.update(org);
            response.status(202);
            return org;
        }, json());
        //funciona
    post("/" + nombre + "/asignar_miembro", (request, response) -> {
            int sectorId = Integer.parseInt(request.queryParams("sectorId"));
            int miembroId = Integer.parseInt(request.queryParams("miembroId"));
            SectorRepositorio repoSector = SectorRepositorio.getInstancia();
            MiembroRepositorio repoMiembro = MiembroRepositorio.getInstancia();
            MiembroPorSectorRepositorio miembroPorSectorRepositorio = MiembroPorSectorRepositorio.getInstancia();
            Miembro miembro = repoMiembro.obtenerMiembroPorId(miembroId).get(0);
            Sector sector = repoSector.obtenerSectorPorId((long) sectorId);

            try{
                sector.aceptarMiembro(miembro);
                MiembroPorSector mps = new MiembroPorSector(miembro, sector, true);
                miembro.addMiembroPorSector(mps);
                repoSector.save(sector);
                MiembroRepositorio.getInstancia().save(miembro);
                miembroPorSectorRepositorio.save(mps);
                response.status(202);
                return "Miembro "+miembro.getNombre()+" "+miembro.getApellido()+ " agregado al sector "+sector.getNombre();
            
            }catch(MiembroNoPuedeUnirseAOrganizacionException e){
                response.status(500);
                return "El miembro ya pertenece a un sector de la organizacion";
            }
        }, json());

        post("/" + nombre + "/asignar_sector", (request, response) -> {
            JSONObject body = new JSONObject(request.body());
            OrganizacionRepositorio orgRepo = OrganizacionRepositorio.getInstancia();
            String nombre = body.getString("nombre");
            Long idOrganizacion = Long.parseLong(body.getString("organizacionId"));
            Organizacion organizacion = orgRepo.getOrganizacion(idOrganizacion);
            Sector sector = new Sector(nombre, organizacion);
            sector = SectorRepositorio.getInstancia().save(sector);

            response.status(202);
            return "Sector "+sector.getNombre()+" agregado a la lista de sectores de la organizacion "+organizacion.getRazon_social();
        }, json());
    }


    public ModelAndView mostrarHome(Request req, Response res) {
        Map<String, Object> parametros = new HashMap<>();
        return new ModelAndView(parametros, "home1.hbs");

    }

    public ModelAndView listadoOrganizaciones(Request req, Response res) {
        Map<String, Object> parametros = new HashMap<>();
        List<Organizacion> organizaciones = this.repo.list();
        parametros.put("organizaciones", organizaciones);
        return new ModelAndView(parametros, "ListadoOrganizaciones.hbs");
    }

    public ModelAndView mostrar(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        Organizacion organizacion = this.repo.get(Long.valueOf(request.params("id")));
        TipoConsumoRepositorio tipoConsumoRepositorio = TipoConsumoRepositorio.getInstancia();
        List<TipoConsumo> tipoconsumos = tipoConsumoRepositorio.list();
        PeriodoDeImputacionRepositorio periodoDeImputacionRepositorio = PeriodoDeImputacionRepositorio.getInstancia();
        List<PeriodoDeImputacion> periodosdeimputacion = periodoDeImputacionRepositorio.list();
        List<PeriodoDeImputacion> periodosDeImputacionAnio = obtenerAniosUnicos(periodosdeimputacion);
        List<PeriodoDeImputacion> periodosDeImputacionMes = obtenerMesesUnicos(periodosdeimputacion);
        MedibleRepositorio medibleRepositorio = MedibleRepositorio.getInstancia();
        List<Medible> medibles = medibleRepositorio.findWithId(organizacion);
        SectorRepositorio sectorRepositorio = SectorRepositorio.getInstancia();

        /*
         * * LoginController.cargarUsuario(parametros,request);
         * if(parametros.get("usuario")==null){
         * response.redirect("/login");
         * }
         */

        parametros.put("medibles", medibles);
        parametros.put("periodosDeImputacionAnio", periodosDeImputacionAnio);
        parametros.put("periodosDeImputacionMes", periodosDeImputacionMes);
        parametros.put("tipoconsumos", tipoconsumos);
        parametros.put("organizacion", organizacion);
        return new ModelAndView(parametros, "detalleOrganizacion.hbs");
    }

    public List<PeriodoDeImputacion> obtenerAniosUnicos(List<PeriodoDeImputacion> periodosDeImputacion) {
        List<PeriodoDeImputacion> periodoAnioUnico = new ArrayList<>();
        int cantidadDePeriodos = periodosDeImputacion.size();
        for (int i = 0; i < cantidadDePeriodos; i++) {
            int anioPeriodo = periodosDeImputacion.get(i).getAnio();
            if (anioNoEstaEnLista(periodoAnioUnico, anioPeriodo)) {
                periodoAnioUnico.add(periodosDeImputacion.get(i));
            }
        }
        return periodoAnioUnico;
    }

    public boolean anioNoEstaEnLista(List<PeriodoDeImputacion> periodoDeImputacion, int anioPeriodo) {
        int largo = periodoDeImputacion.size();
        for (int i = 0; i < largo; i++) {
            if (periodoDeImputacion.get(i).getAnio() == anioPeriodo) {
                return false;
            }
        }
        return true;
    }

    public List<PeriodoDeImputacion> obtenerMesesUnicos(List<PeriodoDeImputacion> periodosDeImputacion) {
        List<PeriodoDeImputacion> periodoMesUnico = new ArrayList<>();
        int cantidadDePeriodos = periodosDeImputacion.size();
        for (int i = 0; i < cantidadDePeriodos; i++) {
            Mes mesPeriodo = periodosDeImputacion.get(i).getMes();
            if (mesNoEstaEnLista(periodoMesUnico, mesPeriodo)) {
                periodoMesUnico.add(periodosDeImputacion.get(i));
            }
        }
        return periodoMesUnico;
    }

    public boolean mesNoEstaEnLista(List<PeriodoDeImputacion> periodoDeImputacion, Mes mesPeriodo) {
        int largo = periodoDeImputacion.size();
        for (int i = 0; i < largo; i++) {
            Mes mes = periodoDeImputacion.get(i).getMes();
            if (mes != null && mes.equals(mesPeriodo)) {
                return false;
            }
        }
        return true;
    }

    public Response calculosHCXAnio(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        Organizacion organizacion = this.repo.get(Long.valueOf(request.params("id")));
        MedibleRepositorio medibleRepositorio = MedibleRepositorio.getInstancia();
        List<Medible> medibles = medibleRepositorio.findWithId(organizacion);
        PeriodoDeImputacionRepositorio periodoDeImputacionRepositorio = PeriodoDeImputacionRepositorio.getInstancia();
        LogCalculosRepositorio logCalculosRepositorio = LogCalculosRepositorio.getInstancia();
        Integer id = Integer.parseInt(request.queryParams("anio"));
        Float calculoMedibles = null;
        PeriodoDeImputacion periodoDeImputacion = null;
        if (request.queryParams("mes").equals("0")) {
            periodoDeImputacion = periodoDeImputacionRepositorio.get(id);
            calculoMedibles = organizacion.calcularHCFechaAnio(periodoDeImputacion.getAnio(), medibles);

        } else {
            // ASK FRANCO 
            int idMes = Integer.parseInt(request.queryParams("mes"));
            Mes mes = periodoDeImputacionRepositorio.get(idMes).getMes();
            int anio = periodoDeImputacionRepositorio.get(id).getAnio();
            System.out.println(mes);
            System.out.println(anio);
            if (!periodoDeImputacionRepositorio.findWithAnioAndMes(anio, mes).equals("0")) { //WARNING: ACA
                periodoDeImputacion = periodoDeImputacionRepositorio.findWithAnioAndMeUno(anio, mes);
                calculoMedibles = organizacion.calcularHCFecha(periodoDeImputacion, medibles);
                /*
                 * LogCalculos calculoLog = new LogCalculos();
                 * calculoLog.setCalculoHC(calculoMedibles);
                 * calculoLog.setOrganizacion(organizacion);
                 * calculoLog.setPeriododeimputacion(periodoDeImputacion);
                 * logCalculosRepositorio.save(calculoLog);
                 */
            }
        }
        parametrosReporte.put("calculoMedibles", calculoMedibles);
        parametrosReporte.put("periodoDeImputacion", periodoDeImputacion);
        response.redirect("/reporteCH");
        return response;
        // return new ModelAndView(parametros, "reporteCH.hbs");
    }

    public ModelAndView mostrarReporte(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        MiembroRepositorio miembroRepositorio = MiembroRepositorio.getInstancia();
        List<Miembro> miembros = miembroRepositorio.list();
        UbicacionRepositorio ubicacionRepositorio = UbicacionRepositorio.getInstancia();
        List<Ubicacion> ubicaciones = ubicacionRepositorio.list();
        MedioDeTransporteRepositorio medioDeTransporteRepositorio = MedioDeTransporteRepositorio.getInstancia();
        List<MedioDeTransporte> mediosDeTransporte = medioDeTransporteRepositorio.list();
        parametros.put("ubicaciones", ubicaciones);
        parametros.put("mediosdetransporte", mediosDeTransporte);
        parametros.put("miembros", miembros);
        return new ModelAndView(parametrosReporte, "reporteCH.hbs");
    }

    public ModelAndView calculosHCXActividad(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        Organizacion organizacion = this.repo.get(Long.valueOf(request.params("id")));
        MedibleRepositorio medibleRepositorio = MedibleRepositorio.getInstancia();
        List<Medible> medibles = medibleRepositorio.findWithId(organizacion);
        ActividadRepositorio actividadRepositorio = ActividadRepositorio.getInstancia();
        List<Actividad> actividades = actividadRepositorio.list();
        List<String> actividadesFiltradasNombre = new ArrayList<>();
       List<Actividad> actividadesFiltradas = new ArrayList<>();

        for (Actividad actividad : actividades) {
            if(!actividadesFiltradasNombre.contains(actividad.getNombre()) && !actividadesFiltradas.contains(actividad)){
                        actividadesFiltradas.add(actividad);
                        actividadesFiltradasNombre.add(actividad.getNombre());
                }
        };
        List<String> nombreActividad = new ArrayList<>();
       List<Float> calculosSinFiltrar = (List<Float>) actividadesFiltradas.stream().map(actividad ->
               actividad.obtenerHC(medibles, organizacion, nombreActividad) )
                .collect(Collectors.toList());

        List<Float> calculos = (List<Float>) calculosSinFiltrar.stream().filter(calculo -> !calculo.equals(0.0f)).collect(Collectors.toList());

        List<String> actividadesFiltradas1 = medibles.stream().map(medible -> medible.getActividad().getNombre()).collect(Collectors.toList());
        //List<String> nombreActividad = new ArrayList<>();
      /*  for(int i = 0; i <calculos.size(); i++) {
            nombreActividad.add(actividadesFiltradas.get(i));
        }*/


        parametros.put("nombreActividad", nombreActividad);
        //parametros.put("nombres", nombres);
        parametros.put("calculos", calculos);
        return new ModelAndView(parametros, "actividad.hbs");

    }

    public ModelAndView calculosHCXSector(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        Organizacion organizacion = this.repo.get(Long.valueOf(request.params("id")));
        SectorRepositorio sectorRepositorio = SectorRepositorio.getInstancia();
        List<Sector> sectoresOrg = sectorRepositorio.findSector(organizacion);
        MiembroPorSectorRepositorio miembroPorSectorRepositorio = MiembroPorSectorRepositorio.getInstancia();
        List<Miembro> miembrosPorSector = new ArrayList<>();



        List<Float> calculos = (List<Float>) sectoresOrg.stream().map(Sector::calcularHC).collect(Collectors.toList());
        List<String> nombreSector = sectoresOrg.stream().map(Sector::getNombre).collect(Collectors.toList());

        /*
         * for (int i = 0; i < nombreSector.size(); i++) {
         * return ("Actividad" + nombreSector + " HC: " + calculos);
         * }
         * return "hola";
         */
        parametros.put("nombreSector", nombreSector);
        parametros.put("calculos", calculos);
        return new ModelAndView(parametros, "porsector.hbs");
    }

    public ModelAndView calculosHCXMiembro(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        Organizacion organizacion = this.repo.get(Long.valueOf(request.params("id")));
        SectorRepositorio sectorRepositorio = SectorRepositorio.getInstancia();
        List<Sector> sectoresOrg = sectorRepositorio.findSector(organizacion);
        MiembroPorSectorRepositorio miembroPorSectorRepositorio = MiembroPorSectorRepositorio.getInstancia();
        List<MiembroPorSector> miembros = miembroPorSectorRepositorio.list();
        List<MiembroPorSector> miembrosFiltrados = miembros.stream()
                .filter(miembroPorSector -> miembroPorSector.getSector().perteneceA(sectoresOrg)).collect(Collectors.toList());
        /*
         * for (Sector sector : sectoresOrg) {
         * miembros.add(miembroPorSectorRepositorio.getMiembroPorSector(sector));
         * }
         */
        List<Float> calculos = new ArrayList<>();
        for(int miembroVuelta = 0; miembroVuelta < miembrosFiltrados.size(); miembroVuelta++){
            MiembroPorSector miembroPorSector = miembrosFiltrados.get(miembroVuelta);
            Miembro miembro = miembroPorSector.getMiembro();

            Float calculo = miembro.calcularHCtotalMiembro(organizacion, miembro);
            calculos.add(calculo);
        }
        /*List<Float> calculos = (List<Float>) miembrosFiltrados.stream()
                .map(miembro -> miembro.getMiembro().calcularHCtotalMiembro(organizacion)).collect(Collectors.toList());*/

        List<String> nombreMiembro = miembrosFiltrados.stream().map(miembro -> miembro.getMiembro().getNombre())
                .collect(Collectors.toList());
        /*
         * for (int i = 0; i < nombreSector.size(); i++) {
         * return ("Actividad" + nombreSector + " HC: " + calculos);
         * }
         * return "hola";
         */
        parametros.put("nombreMiembro", nombreMiembro);
        parametros.put("calculos", calculos);
        return new ModelAndView(parametros, "miembro.hbs");
    }

    public ModelAndView detalleNoRegistrado(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        Organizacion organizacion = this.repo.get(Long.valueOf(request.params("id")));
        parametros.put("organizacion", organizacion);
        return new ModelAndView(parametros, "detalleOrg.html");
    }
}
