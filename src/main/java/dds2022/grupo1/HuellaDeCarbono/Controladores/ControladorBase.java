package dds2022.grupo1.HuellaDeCarbono.Controladores;

import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;

import spark.Request;
import static spark.Spark.*;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.RepositorioBase;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoImplementadoException;

public class ControladorBase<T> {
    // provee CRUD para el modelo que instancie
    RepositorioBase<T> repo;
    String nombre = "api/";

    protected Long parsePK(Request request) {
        return Long.parseLong(request.params(":pk"));
    }

    protected int parsePKInt(Request request) {
        return Integer.parseInt(request.params(":pk"));
    }

    public void setRepo() throws NoImplementadoException {
        throw new NoImplementadoException("");
    }

    public ControladorBase(String nombre) {
        this.nombre += nombre;
        try {
            setRepo();
        } catch (NoImplementadoException e) {
            throw new RuntimeException("No se implemento un repositorio para esta clase");
        }

        runBaseEndpoints();

        exception(NoExisteException.class, (e, request, response) -> {
            response.status(404);
            response.body(String.format("No se encontraron los recursos buscados para el modelo " + nombre));
        });
    }

    protected void runListEndpoint() {
        // GET /{model}/
        get("/" + nombre, (req, res) -> {
            res.status(202);
            return repo.list();
        }, json());
            
    }

    protected void runBaseEndpoints() {
        /*
         * Crea los metodos List, Get y Delete automaticamente
         */
        runListEndpoint();

        // GET /{model}/123
        get("/" + nombre + "/:pk", (request, response) -> {
            response.status(200);
            try{
                return repo.get(parsePK(request));
            } catch (Exception e) {
                return repo.get(parsePKInt(request));
            }
        }, json());

        // DELETE /{model}/123
        delete("/" + nombre + "/:pk", (request, response) -> {
            try{
                repo.delete(parsePK(request));
            } catch (Exception e) {
                repo.delete(parsePKInt(request));
            }
            response.status(202);
            return "Se borro con exito el obj con id: " + parsePK(request);
        });
    }
}


