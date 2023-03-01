package dds2022.grupo1.HuellaDeCarbono.Controladores;

import static spark.Spark.*;

import org.json.JSONObject;
import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.AgenteRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.SectorTerritorialRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.Agente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.SectorTerritorial;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.DTOFactory;

public class ControladorAgente extends ControladorBase<Agente> {

    @Override
    public void setRepo() {
        repo = AgenteRepositorio.getInstancia();
    }

    public ControladorAgente() {
        super("agentes");
        post("/" + nombre, (request, response) -> {
            JSONObject body = new JSONObject(request.body());
            Agente agente = DTOFactory.getAgente(body);
            if(body.has("sector_id")){
                SectorTerritorial sectorPers = SectorTerritorialRepositorio.getInstancia().get(body.getInt("sector_id"));
                agente.setSector(sectorPers);
            }

            agente = repo.save(agente);
            return agente;
        }, json());

        put("/" + nombre + "/:pk", (request, response) -> {
            JSONObject body = new JSONObject(request.body());

            Agente agente = DTOFactory.getAgente(body);
            agente.setId(parsePKInt(request));
            if(body.has("sector_id")){
                SectorTerritorial sectorPers = SectorTerritorialRepositorio.getInstancia().get(body.getInt("sector_id"));
                agente.setSector(sectorPers);
            }
            repo.update(agente);

            return agente;
        }, json());

    }
}
