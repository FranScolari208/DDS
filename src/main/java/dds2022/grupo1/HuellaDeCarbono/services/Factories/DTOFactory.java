package dds2022.grupo1.HuellaDeCarbono.services.Factories;

import java.util.HashMap;
import java.util.Map;

import dds2022.grupo1.HuellaDeCarbono.DTOs.*;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.OrganizacionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import org.json.JSONObject;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoOrganizacionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.TipoOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.Agente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Clasificacion;

public class DTOFactory {
    public static OrganizacionDTO getOrganizacionDTO(JSONObject body) {
        if (body.has("id")) {
            return new OrganizacionDTO(
                    body.getInt("id"),
                    body.getString("imagen"),
                    body.getString("razonSocial"),
                    body.getJSONObject("ubicacion"), // cuidado con cambios
                    body.getString("clasificacion"),
                    body.getString("tipoOrganizacion"));
        }
        return new OrganizacionDTO(
                body.getString("razonSocial"),
                body.getString("imagen"),
                body.getJSONObject("ubicacion"), // cuidado con cambios
                body.getString("clasificacion"),
                body.getString("tipoOrganizacion"));
    }

    public static Organizacion getOrganizacion(OrganizacionDTO orgDTO) {

        JSONObject ubicacion = orgDTO.getUbicacion();
        Map<String, String> params = new HashMap<String, String>();
        params.put("descripcion", orgDTO.getTipoOrganizacion());
        TipoOrganizacion tipoOrganizacion = TipoOrganizacionRepositorio.getInstancia().getByStringValues(params, false);
        // TipoOrganizacion tipoOrganizacion =
        // TipoOrganizacionRepository.getInstancia().getTipoOrganizacion(tipoOrg);
        Clasificacion clasif = Clasificacion.valueOf(orgDTO.getClasificacion());

        Ubicacion ubi = new Ubicacion(ubicacion.getString("direccion"), ubicacion.getString("altura"), ubicacion.getFloat("latitud"),
                ubicacion.getFloat("longitud"), ubicacion.getString("localidad"), ubicacion.getString("provincia"), ubicacion.getString("municipio"));

        return new Organizacion(orgDTO.getRazonSocial(), orgDTO.getImagen(), tipoOrganizacion, ubi, clasif);
    }

    public static Organizacion getOrganizacion(JSONObject body) {
        return getOrganizacion(getOrganizacionDTO(body));

    }


    public static AgenteDTO getAgenteDTO(JSONObject body) {
        AgenteDTO agenteDto = new AgenteDTO(
            body.getString("nombre"),
            body.getString("telefono"),
            body.getString("email")
        );
        if (body.has("id")) {
            agenteDto.setId(body.getInt("id"));
        }

        if (body.has("sector_id")){
            agenteDto.setSector(body.getInt("sector_id"));
        }
        return agenteDto;
    }

    public static Agente getAgente(AgenteDTO agente) {
        return new Agente(
                agente.getNombre(),
                agente.getEmail(),
                agente.getTelefono());
    }

    public static Agente getAgente(JSONObject body) {
        return getAgente(getAgenteDTO(body));
    }

    public static TrayectoDTO getTrayectoDTO(JSONObject body) {
        // para aclarar:
        // el array de medio de transporte va a tener el siguiente formato:
        // ['bicicleta'] || ['auto', 'NAFTA'] || ['colectivo', 'Linea xx']

        // TODO: checkear que seguro no nos van a estar llegando los miembros (hay un
        // endpoint separado para esto)
        if (body.has("id")) {
            return new TrayectoDTO(
                    body.getInt("id"),
                    body.getJSONObject("salida"),
                    body.getJSONObject("destino"),
                    body.getJSONArray("transporte"),
                    body.getString("imputacion"),
                    body.getJSONArray("tramos"),
                    body.getInt("participantes"),
                    body.getJSONArray("miembros"));
        }
        return new TrayectoDTO(
                body.getJSONObject("salida"),
                body.getJSONObject("destino"),
                body.getJSONArray("transporte"),
                body.getString("imputacion"),
                body.getJSONArray("tramos"),
                body.getInt("participantes"),
                body.getJSONArray("miembros"));
    }

    public static Trayecto getTrayecto(TrayectoDTO trayecto) {

        return new Trayecto(
                trayecto.getUbicacionSalida(),
                trayecto.getUbicacionLlegada(),
                MedioDeTransporteFactory.crearPorTipoVehiculo(trayecto.getMedioDeTransporte()),
                trayecto.getTramos(),
                trayecto.getCantidadParticipantes());
    }

    public static Trayecto getTrayecto(JSONObject body) {
        return getTrayecto(getTrayectoDTO(body));
    }

// TODO revisar que cambios hubo en este archivo
    public static TramoDTO getTramoDTO(JSONObject body) {
        if (body.has("id")) {
            return new TramoDTO(
                    body.getInt("id"),
                    body.getJSONObject("ubicacionActual"), // cuidado con cambios
                    body.getJSONObject("ubicacionAnterior"),
                    body.getJSONObject("ubicacionProxima"),
                    body.getJSONObject("trayecto"));
        }
        return new TramoDTO(
                body.getInt("id"),
                body.getJSONObject("ubicacionActual"), // cuidado con cambios
                body.getJSONObject("ubicacionProxima"),
                body.getJSONObject("trayecto"));
    }

    public static Tramo getTramo(TramoDTO tramoDTO) {

        JSONObject ubicacionActual = tramoDTO.getUbicacionActual();
        JSONObject ubicacionAnterior = tramoDTO.getUbicacionAnterior();
        JSONObject ubicacionProxima = tramoDTO.getUbicacionProxima();
        JSONObject tray = tramoDTO.getTrayecto();


        Ubicacion ubiActual = new Ubicacion(ubicacionActual.getString("direccion"), ubicacionActual.getFloat("latitud"),
                ubicacionActual.getFloat("longitud"));
        Ubicacion ubiAnterior = new Ubicacion(ubicacionAnterior.getString("direccion"), ubicacionAnterior.getFloat("latitud"),
                ubicacionAnterior.getFloat("longitud"));
        Ubicacion ubiProxima = new Ubicacion(ubicacionProxima.getString("direccion"), ubicacionProxima.getFloat("latitud"),
                ubicacionProxima.getFloat("longitud"));

        
        Trayecto trayecto = getTrayecto(tray);

        return new Tramo(ubiActual, ubiAnterior, ubiProxima, trayecto);
    }

    public static Tramo getTramo(JSONObject body) {
        return getTramo(getTramoDTO(body));

    }
}
