package server;
import Spark.utils.BooleanHelper;
import Spark.utils.HandlebarsTemplateEngineBuilder;
import dds2022.grupo1.HuellaDeCarbono.Controladores.ControladorOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.Controladores.ControladorTramo;
import dds2022.grupo1.HuellaDeCarbono.Controladores.ControladorTrayectos;
import dds2022.grupo1.HuellaDeCarbono.Controladores.LoginController;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Router {
    private static HandlebarsTemplateEngine engine;

    private static void initEngine() {
        Router.engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
              //  .withHelper("increment", IndexHelper.increment)
              //  .withHelper("append", StringHelper.append)
              //  .withHelper("selected", StringHelper.selected)
                .build();
    }

    public static void init() {
        Router.initEngine();
        Spark.staticFileLocation("/public");
        Router.configure();
    }

    private static void configure() {

        ControladorOrganizacion homeControler = new ControladorOrganizacion();
        ControladorTramo controladorTramo = new ControladorTramo();
        ControladorTrayectos controladorTrayectos = new ControladorTrayectos();
        LoginController loginController = new LoginController();
        Spark.get("/", loginController::login,engine);
        Spark.get("/home1", homeControler::mostrarHome, engine);
        Spark.get("/ListadoOrganizaciones",loginController::mostrarOrganizaciones, engine);
        Spark.get("/detalleOrganizacion/:id",homeControler::mostrar,engine);
        Spark.get("/RegistrarTramo/:id",controladorTramo::mostrar, engine);
        Spark.get("/RegistrarTrayecto", controladorTrayectos::mostrar,engine);
        Spark.post("/RegistrarTramo/:id",controladorTramo::guardar);
        Spark.post("/RegistrarTrayecto",controladorTrayectos::guardar);
        Spark.post("/detalleOrganizacion/:id", homeControler::calculosHCXAnio);
        Spark.get("/reporteCH", homeControler::mostrarReporte,engine);
        Spark.post("/reporteCH",homeControler::calculosHCXAnio);
        Spark.get("/porsector/:id", homeControler::calculosHCXSector,engine);
        Spark.get("/login", loginController::login,engine);
        Spark.get("/loginError", loginController::loginError,engine);
        Spark.post("/login",loginController::iniciarSesion);
        Spark.get("/listadoTrayectos", controladorTrayectos::listadoTrayectos, engine);
        Spark.get("/actividad/:id", homeControler::calculosHCXActividad,engine);
        Spark.get("/miembro/:id", homeControler::calculosHCXMiembro,engine);
        Spark.get("/EditarTrayecto/:id",controladorTrayectos::modificar,engine);
        Spark.post("/EditarTrayecto/:id",controladorTrayectos::guardarModificado, engine);
        Spark.get("/organizaciones",loginController::mostrarOrganizacionesNoRegistrado, engine);
        Spark.get("/organizaciones/:id",homeControler::detalleNoRegistrado,engine);

}}