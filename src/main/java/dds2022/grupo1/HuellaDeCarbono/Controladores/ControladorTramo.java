package dds2022.grupo1.HuellaDeCarbono.Controladores;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.DTOFactory;
import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import static spark.Spark.*;

public class ControladorTramo extends ControladorBase<Tramo> {
    public void setRepo() {
        repo = TramoRepositorio.getInstancia();
    }

    public ControladorTramo() {
        super("tramos");// todo refactorizar a nuevo



/*
 * 
 * 
 * UbicacionRepositorio ubiRepo = UbicacionRepositorio.getInstancia();
            JSONObject body = new JSONObject(request.body());
            Trayecto trayecto = DTOFactory.getTrayecto(body);

            Ubicacion salidaPers = ubiRepo.save(trayecto.getSalida());
            Ubicacion destinoPers = ubiRepo.save(trayecto.getDestino());
            trayecto.setSalida(salidaPers);
            trayecto.setDestino(destinoPers);

            MedioDeTransporte transPers = MedioDeTransporteRepositorio.getInstancia()
                .save(trayecto.getMedioDeTransporte());
            trayecto.setMedioDeTransporte(transPers);

            PeriodoDeImputacion periodPers = PeriodoDeImputacionRepositorio.getInstancia().save(trayecto.getPeriodoDeImputacion());
            trayecto.setPeriodoDeImputacion(periodPers);
            
            trayecto = repo.save(trayecto);
 * 
 * 
*/


        post("/" + nombre, (request, response) -> {
            //TODO: hacer que funcione para la nueva version de tramo :D
            JSONObject body = new JSONObject(request.body());
            UbicacionRepositorio ubiRepo = UbicacionRepositorio.getInstancia();
            Tramo tramo = DTOFactory.getTramo(body);


            // chequeo existencia de ubicaciones
            Ubicacion ant = ubiRepo.save(tramo.getUbicacionAnterior());
            Ubicacion act = ubiRepo.save(tramo.getUbicacionActual());
            Ubicacion prox = ubiRepo.save(tramo.getUbicacionProxima());
            
            tramo.setUbicacionAnterior(ant);
            tramo.setUbicacionActual(act);
            tramo.setUbicacionProxima(prox);

            Trayecto tray = TrayectoRepositorio.getInstancia().save(tramo.getTrayecto());
            tramo.setTrayecto(tray);

            tramo = repo.save(tramo);
            response.status(200);
            return tramo;
        }, json());

        put("/" + nombre + "/:tramoId", (request, response) -> {
            UbicacionRepositorio ubiRepo = UbicacionRepositorio.getInstancia();          
            int tramoId = Integer.parseInt(request.params("tramoId"));
            JSONObject body = new JSONObject(request.body());
            Tramo tramo = DTOFactory.getTramo(body);
            tramo.setId(tramoId);
            // chequeo existencia de ubicaciones
            Ubicacion ant = ubiRepo.save(tramo.getUbicacionAnterior());
            Ubicacion act = ubiRepo.save(tramo.getUbicacionActual());
            Ubicacion prox = ubiRepo.save(tramo.getUbicacionProxima());
            
            tramo.setUbicacionAnterior(ant);
            tramo.setUbicacionActual(act);
            tramo.setUbicacionProxima(prox);

            Trayecto tray = TrayectoRepositorio.getInstancia().save(tramo.getTrayecto());
            tramo.setTrayecto(tray);        

            repo.update(tramo);
            response.status(200);
            return tramo;
        }, json());

        post("/" + nombre + "/asignar_miembro", (request, response) -> {
            int sectorId = Integer.parseInt(request.queryParams("sectorId"));
            int miembroId = Integer.parseInt(request.queryParams("miembroId"));
            SectorRepositorio repoSector = SectorRepositorio.getInstancia();
            MiembroRepositorio repoMiembro = MiembroRepositorio.getInstancia();

            Sector sector = repoSector.get(sectorId);
            Miembro miembro = repoMiembro.get(miembroId);
            sector.addMiembro(miembro);
            repoSector.update(sector);
            response.status(200);

            return "Se asigno miembro de forma exitosa :D";
        });
    }

    public ModelAndView mostrar(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        UbicacionRepositorio ubicacionRepositorio = UbicacionRepositorio.getInstancia();
        List<Ubicacion> ubicaciones = ubicacionRepositorio.list();
        TrayectoRepositorio trayectoRepositorio = TrayectoRepositorio.getInstancia();
        Trayecto trayecto = trayectoRepositorio.get(Integer.parseInt(request.params("id")));
        // Long.valueOf(request.params("id"))
        parametros.put("ubicaciones", ubicaciones);
        parametros.put("trayecto", trayecto);
        return new ModelAndView(parametros, "RegistrarTramo.hbs");
    }

    private void asignarParametrosATramo(Tramo tramo, Request request) {
        // Persona duenioMascota = this.obtenerPersona(request);
        UbicacionRepositorio ubicacionRepositorio = UbicacionRepositorio.getInstancia();
        Long idUbiActual = Long.parseLong(request.queryParams("ubicacionactual"));
        Long idUbiAnterior = Long.parseLong(request.queryParams("ubicacionanterior"));
        Long idUbiProxima = Long.parseLong(request.queryParams("ubicacionproxima"));
        Ubicacion ubicacionesActual = ubicacionRepositorio.get(idUbiActual);
        Ubicacion ubicacionAnterior = ubicacionRepositorio.get(idUbiAnterior);
        Ubicacion ubicacionProxima = ubicacionRepositorio.get(idUbiProxima);
        tramo.setUbicacionActual(ubicacionesActual);
        tramo.setUbicacionAnterior(ubicacionAnterior);
        tramo.setUbicacionProxima(ubicacionProxima);

    }

    public Response guardar(Request request, Response response) {
        // request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
        // new MultipartConfigElement("/tmp", 100000000, 100000000, 1024));
        Tramo tramo = new Tramo();
        TrayectoRepositorio trayectoRepositorio = TrayectoRepositorio.getInstancia();
        Trayecto trayecto = trayectoRepositorio.get(Integer.parseInt(request.params("id")));
        asignarParametrosATramo(tramo, request);
        tramo.setTrayecto(trayecto);
        this.repo.save(tramo);
        // List<String> fotos = MascotaEncontradaController.guardarImagenes(request,
        // mascota.getId(), "mascotas");
        // mascota.setFotosUrl(fotos);
        // repo.modificar(tramo);
        response.redirect("/home1");
        return response;
    }

}
