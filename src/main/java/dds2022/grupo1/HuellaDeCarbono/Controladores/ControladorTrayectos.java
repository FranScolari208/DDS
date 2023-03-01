package dds2022.grupo1.HuellaDeCarbono.Controladores;

import static spark.Spark.*;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;
import org.json.JSONObject;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.DTOFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
// import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorTrayectos extends ControladorBase<Trayecto> {

    public ControladorTrayectos() {
        super("trayectos");
        //funciona
        post("/" + nombre + "/:pk/asignar_miembro/:miembroId", (request, response) -> {
            int miembroId = Integer.parseInt(request.params(":miembroId"));
            Trayecto tray = repo.get(parsePKInt(request));
            MiembroRepositorio miere = MiembroRepositorio.getInstancia();
            Miembro miembro = miere.get(miembroId);
            List<Trayecto> trayectosMiembro = miembro.getTrayectos();
            trayectosMiembro.add(tray);
            miembro.setTrayectos(trayectosMiembro);
            tray.setCantidadParticipantes(tray.getCantidadParticipantes()+1);
            tray.addMiembro(miembro);
            miere.update(miembro);
            repo.update(tray);
            response.status(202);
            return tray;
        }, json());

    }

    @Override
    public void setRepo() {
        repo = TrayectoRepositorio.getInstancia();
    }

    @Override
    protected void runBaseEndpoints() {
        get("/" + nombre, (req, res) -> {
            res.status(202);
            return repo.list();
        }, json());

        post("/" + nombre, (request, response) -> {
            UbicacionRepositorio ubiRepo = UbicacionRepositorio.getInstancia();
            JSONObject body = new JSONObject(request.body());
            Trayecto trayecto = DTOFactory.getTrayecto(body);

            Ubicacion salidaPers = ubiRepo.save(trayecto.getSalida());
            Ubicacion destinoPers = ubiRepo.save(trayecto.getDestino());
            trayecto.setSalida(salidaPers);
            trayecto.setDestino(destinoPers);

            MedioDeTransporte transPers = MedioDeTransporteRepositorio.getInstancia()
                .save(trayecto.getMedioDeTransporte());
            trayecto.setMedioDeTransporte(transPers);
            if (trayecto.getPeriodoDeImputacion() != null) {
                PeriodoDeImputacion periodPers = PeriodoDeImputacionRepositorio.getInstancia().save(trayecto.getPeriodoDeImputacion());
                trayecto.setPeriodoDeImputacion(periodPers);
            }
            
            trayecto = repo.save(trayecto);
            response.status(202);

            return trayecto;
        }, json());

        put("/" + nombre + "/:pk", (request, response) -> {
            UbicacionRepositorio ubiRepo = UbicacionRepositorio.getInstancia();

            JSONObject body = new JSONObject(request.body());
            Trayecto trayecto = DTOFactory.getTrayecto(body);
            trayecto.setId(parsePKInt(request));

            Ubicacion salidaPers = ubiRepo.save(trayecto.getSalida());
            Ubicacion destinoPers = ubiRepo.save(trayecto.getDestino());
            trayecto.setSalida(salidaPers);
            trayecto.setDestino(destinoPers);

            MedioDeTransporte transPers = MedioDeTransporteRepositorio.getInstancia()
                .save(trayecto.getMedioDeTransporte());
            trayecto.setMedioDeTransporte(transPers);

            PeriodoDeImputacion periodPers = PeriodoDeImputacionRepositorio.getInstancia().save(trayecto.getPeriodoDeImputacion());
            trayecto.setPeriodoDeImputacion(periodPers);

            repo.update(trayecto);
            return trayecto;
        }, json());
    }

    public ModelAndView mostrar(Request request, Response response) {
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
        return new ModelAndView(parametros, "RegistrarTrayecto.hbs");
    }

    public ModelAndView guardarModificado(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        Miembro miembro = request.session().attribute("miembro");
        List<Trayecto> trayectos = miembro.getTrayectos();
        UbicacionRepositorio ubicacionRepositorio = UbicacionRepositorio.getInstancia();
        TrayectoRepositorio trayectoRepositorio = TrayectoRepositorio.getInstancia();
        MedioDeTransporteRepositorio medioDeTransporteRepositorio = MedioDeTransporteRepositorio.getInstancia();
        MiembroRepositorio miembroRepositorio = MiembroRepositorio.getInstancia();
        Trayecto trayecto = trayectoRepositorio.getById(Integer.parseInt(request.params("id"))).get(0); //WARNING: ACA
        String ubicacionSalida = request.queryParams("ubicacionsalida");
        String ubicacionDestino = request.queryParams("ubicaciondestino");
        String transporte = request.queryParams("tipotransporte");
        String idMiembroString = request.queryParams("miembroAgregar");
        String idMiembroQuitarString = request.queryParams("miembroQuitar");

        int idMiembro = 0;
        int idMiembroQuitar = 0;

        if (idMiembroString != null) {
            idMiembro = Integer.parseInt(idMiembroString);
        }
        if (idMiembroQuitarString != null) {
            idMiembroQuitar = Integer.parseInt(idMiembroQuitarString);
        }
        Ubicacion ubicacion1 = ubicacionRepositorio.getBy("direccion", ubicacionSalida);
        Ubicacion ubicacion2 = ubicacionRepositorio.getBy("direccion", ubicacionDestino);
        MedioDeTransporte transporteTrayecto = medioDeTransporteRepositorio.getBy("descripcion", transporte);

        if (idMiembro != 0) {
            Miembro miembroParaAgregar = miembroRepositorio.get(idMiembro);
            trayecto.addMiembro(miembroParaAgregar);
            trayecto.setCantidadParticipantes(trayecto.getMiembros().size());

            trayectoRepositorio.update(trayecto);

            List<Trayecto> trayectosMiembro = miembroParaAgregar.getTrayectos();
            trayectosMiembro.add(trayecto);
            miembroParaAgregar.setTrayectos(trayectosMiembro);
            miembroRepositorio.update(miembroParaAgregar);

        }

        if (idMiembroQuitar != 0) {
            Miembro miembroParaQuitar = miembroRepositorio.get(idMiembroQuitar);
            List<Miembro> miembrosTrayecto = new ArrayList<>();
            List<Trayecto> trayectosMiembroNuevo = new ArrayList<>();
            for (int i = 0; i < trayecto.getMiembros().size(); i++) {
                if (miembroParaQuitar.getId() != trayecto.getMiembros().get(i).getId()) {
                    miembrosTrayecto.add(trayecto.getMiembros().get(i));
                }
            }
            trayecto.setMiembros(miembrosTrayecto);
            trayecto.setCantidadParticipantes(miembrosTrayecto.size());

            trayectoRepositorio.update(trayecto);

            List<Trayecto> trayectosMiembro = miembroParaQuitar.getTrayectos();
            for (int i = 0; i < trayectosMiembro.size(); i++) {
                Trayecto t = trayectosMiembro.get(i);
                if (t.getId() != trayecto.getId()) {
                    trayectosMiembroNuevo.add(t);
                }
            }
            miembroParaQuitar.setTrayectos(trayectosMiembroNuevo);
            miembroRepositorio.update(miembroParaQuitar);

        }
        trayecto.setSalida(ubicacion1);
        trayecto.setDestino(ubicacion2);
        trayecto.setMedioDeTransporte(transporteTrayecto);
        trayectoRepositorio.update(trayecto);
        parametros.put("trayectos", trayectos);
        return new ModelAndView(parametros, "listadoTrayectos.hbs");
    }

    private void asignarParametrosATrayecto(Trayecto trayecto, Request request) {
        UbicacionRepositorio ubicacionRepositorio = UbicacionRepositorio.getInstancia();
        MiembroRepositorio miembroRepositorio = MiembroRepositorio.getInstancia();
        MedioDeTransporteRepositorio medioDeTransporteRepositorio = MedioDeTransporteRepositorio.getInstancia();
        Long idUbiSalida = Long.parseLong(request.queryParams("ubicacionsalida"));
        Long idUbiDestino = Long.parseLong(request.queryParams("ubicaciondestino"));
        String miembroCompartido = request.queryParams("miembro");
        if (!miembroCompartido.equals("0")) {
            int idMiembro = Integer.parseInt(request.queryParams("miembro"));
            Miembro miembroAgregar = miembroRepositorio.get(idMiembro);
            trayecto.addMiembro(miembroAgregar);
            miembroAgregar.addTrayecto(trayecto);

            trayecto.setCantidadParticipantes(trayecto.getCantidadParticipantes() + 1);
        }
        int idMedioDeTransporte = Integer.parseInt(request.queryParams("tipotransporte"));
        Ubicacion ubicacionesSalida = ubicacionRepositorio.get(idUbiSalida);
        Ubicacion ubicacionDestino = ubicacionRepositorio.get(idUbiDestino);
        MedioDeTransporte medioDeTransporte = medioDeTransporteRepositorio.get(idMedioDeTransporte);
        trayecto.setSalida(ubicacionesSalida);
        trayecto.setDestino(ubicacionDestino);
        trayecto.setMedioDeTransporte(medioDeTransporte);
        Miembro miembro = request.session().attribute("miembro");
        int idMiembro = miembro.getId();
        Miembro miembroAgregar = miembroRepositorio.get(idMiembro);
        trayecto.addMiembro(miembroAgregar);
        miembroAgregar.addTrayecto(trayecto);
        miembro.addTrayecto(trayecto);

    }

    public Response guardar(Request request, Response response) {
        // request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
        // new MultipartConfigElement("/tmp", 100000000, 100000000, 1024));
        Trayecto trayecto = new Trayecto();
        asignarParametrosATrayecto(trayecto, request);
        this.repo.save(trayecto);
        // List<String> fotos = MascotaEncontradaController.guardarImagenes(request,
        // mascota.getId(), "mascotas");
        // mascota.setFotosUrl(fotos);
        // repo.modificar(tramo);
        response.status(200);
        response.redirect("/home1");
        return response;
    }

    public ModelAndView listadoTrayectos(Request req, Response res) {
        Map<String, Object> parametros = new HashMap<>();
        // List<Trayecto> trayectos = this.repo.list();
        Miembro miembro = req.session().attribute("miembro");
        List<Trayecto> trayectos = miembro.getTrayectos();
        parametros.put("trayectos", trayectos);
        return new ModelAndView(parametros, "listadoTrayectos.hbs");
    }

    public ModelAndView modificar(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        MiembroRepositorio miembroRepositorio = MiembroRepositorio.getInstancia();
        List<Miembro> miembros = miembroRepositorio.list();
        TrayectoRepositorio trayectoRepositorio = TrayectoRepositorio.getInstancia();
        Trayecto trayecto = trayectoRepositorio.getById(Integer.parseInt(request.params("id"))).get(0); //WARNING: ACA
        UbicacionRepositorio ubicacionRepositorio = UbicacionRepositorio.getInstancia();
        List<Ubicacion> ubicaciones = ubicacionRepositorio.list();
        MedioDeTransporteRepositorio medioDeTransporteRepositorio = MedioDeTransporteRepositorio.getInstancia();
        List<MedioDeTransporte> mediosDeTransporte = medioDeTransporteRepositorio.list();
        parametros.put("ubicacionSalida", trayecto.getSalida().getDireccion());
        parametros.put("ubicacionDestino", trayecto.getDestino().getDireccion());
        parametros.put("medioDeTransporte", trayecto.getMedioDeTransporte().getDescripcion());
        parametros.put("ubicacionesdestino", ubicacionesSinLaDelTrayecto(ubicaciones, trayecto.getDestino()));
        parametros.put("ubicacionessalida", ubicacionesSinLaDelTrayecto(ubicaciones, trayecto.getSalida()));
        parametros.put("mediosdetransporte",
                mediosDeTransporteSinElDelTrayecto(mediosDeTransporte, trayecto.getMedioDeTransporte()));
        parametros.put("miembroAgregar", miembrosQueNoEstanEnTrayecto(trayecto, miembros));
        parametros.put("miembroQuitar",
                miembrosQueEstanEnTrayectoMenosActual(trayecto, request.session().attribute("miembro")));
        parametros.put("trayecto", trayecto);
        return new ModelAndView(parametros, "editarTrayecto.hbs");
    }

    private List<Miembro> miembrosQueEstanEnTrayectoMenosActual(Trayecto trayecto, Miembro miembro) {
        List<Miembro> miembrosParaMostrar = new ArrayList<>();
        int largo = trayecto.getMiembros().size();

        for (int i = 0; i < largo; i++) {
            Miembro m = trayecto.getMiembros().get(i);
            if (m.getId() != miembro.getId()) {
                miembrosParaMostrar.add(m);
            }
        }
        return miembrosParaMostrar;
    }

    public List<Miembro> miembrosQueNoEstanEnTrayecto(Trayecto trayecto, List<Miembro> miembros) {
        List<Miembro> miembrosParaMostrar = new ArrayList<>();
        List<Miembro> miembrosTrayecto = trayecto.getMiembros();
        int largo = miembros.size();
        for (int i = 0; i < largo; i++) {
            Miembro m = miembros.get(i);
            if (!miembroEstaEnTrayecto(miembrosTrayecto, m)) {
                miembrosParaMostrar.add(m);
            }
        }
        return miembrosParaMostrar;
    }

    public boolean miembroEstaEnTrayecto(List<Miembro> miembrosTrayecto, Miembro miembro) {
        for (int i = 0; i < miembrosTrayecto.size(); i++) {
            if (miembrosTrayecto.get(i).getId() == miembro.getId()) {
                return true;
            }
        }
        return false;
    }

    public List<MedioDeTransporte> mediosDeTransporteSinElDelTrayecto(List<MedioDeTransporte> medioDeTransportes,
            MedioDeTransporte transporte) {
        List<MedioDeTransporte> medioDeTransporte = new ArrayList<>();
        int cantidad = medioDeTransportes.size();
        for (int i = 0; i < cantidad; i++) {
            MedioDeTransporte m = medioDeTransportes.get(i);
            if (!m.getDescripcion().equals(transporte.getDescripcion())) {
                medioDeTransporte.add(m);
            }
        }
        return medioDeTransporte;
    }

    public List<Ubicacion> ubicacionesSinLaDelTrayecto(List<Ubicacion> ubicaciones, Ubicacion ubicacionDestino) {
        List<Ubicacion> ubicacionesDevueltas = new ArrayList<>();
        int cantidad = ubicaciones.size();
        for (int i = 0; i < cantidad; i++) {
            Ubicacion m = ubicaciones.get(i);
            if (!m.getDireccion().equals(ubicacionDestino.getDireccion())) {
                ubicacionesDevueltas.add(m);
            }
        }
        return ubicacionesDevueltas;
    }
}
