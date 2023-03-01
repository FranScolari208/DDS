package dds2022.grupo1.HuellaDeCarbono.Controladores;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.MedicionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.OrganizacionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Medicion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Mes;
import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControladorMedicion extends ControladorBase<Medicion> {
    @Override
    public void setRepo() {
        repo = MedicionRepositorio.getInstancia();
    }

    public ControladorMedicion() {
        super("mediciones");
    }

    @Override
    protected void runBaseEndpoints() {
        get("/" + nombre, (req, res) -> {
            // consulta -> filtros
            // filtros: PeriodoDeImputacion, empresa,
            // anio / anio&mes / razonSocial
            String anio = req.queryParams("anio");
            String mes = req.queryParams("mes");
            String razonSocial = req.queryParams("razonSocial");
            List<Medicion> mediciones = repo.list();
            if (req.queryParams().isEmpty()) {
                return mediciones;
            }
            if (razonSocial != null) {
                Map<String, String> query = new HashMap<>();
                query.put("razon_social", razonSocial);
                OrganizacionRepositorio orgRepo = OrganizacionRepositorio.getInstancia();
                Organizacion org = orgRepo.getByStringValues(query, false);
                // TODO: hacer que esta query este en el repo de cada modelo!!
                mediciones = (List<Medicion>) (List<?>) org.getMedibles();
            }
            if (anio != null) {
                mediciones = mediciones.stream().filter((medi) -> {
                    Medicion medicion = (Medicion) medi;
                    PeriodoDeImputacion periodo = medicion.getPeriodoImputacion();
                    return periodo.getAnio() == Integer.parseInt(anio);
                }).collect(Collectors.toList());
            }
            if (mes != null) {
                mediciones = mediciones.stream().filter((medi) -> {
                    Medicion medicion = (Medicion) medi;
                    PeriodoDeImputacion periodo = medicion.getPeriodoImputacion();
                    return periodo.getMes() == Mes.getMesPorNumero(Integer.parseInt(mes));
                }).collect(Collectors.toList());
            }

            return mediciones;
        }, json());
    }
}
