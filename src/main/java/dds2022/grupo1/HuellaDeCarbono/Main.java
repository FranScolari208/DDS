package dds2022.grupo1.HuellaDeCarbono;

import db.EntityManagerHelper;
import dds2022.grupo1.HuellaDeCarbono.Controladores.*;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Adaptador;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.*;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.ActividadRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.Agente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.SectorTerritorial;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.Parada;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TipoTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteEcologico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteParticular;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransportePublico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.*;
import dds2022.grupo1.HuellaDeCarbono.services.ServicioGeoref;
import dds2022.grupo1.HuellaDeCarbono.services.Parsers.ParserCSV;
import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.MultipartConfigElement;

import org.json.JSONObject;
import server.Router;
import spark.Spark;
import spark.debug.DebugScreen;

public class Main {

        private static int getPort() {
                String herokuPort = System.getenv("PORT");
                if (herokuPort != null) {
                        return Integer.parseInt(herokuPort);
                }
                return 80;
        }

        public static void main(String[] args) throws IOException, URISyntaxException {
                // aca van los controladores de cada modelo que expongamos en la api
                //startEntityManagerFactory();
                Spark.port(getPort());
                Router.init();
                DebugScreen.enableDebugScreen();

                new ControladorOrganizacion();
                new ControladorMedicion();

                new ControladorAgente();
                new ControladorBatchMedicion();

                new ControladorTrayectos();

                get("/api/tipo_consumo", (request, response) -> {
					TipoConsumoRepositorio repoTC = TipoConsumoRepositorio.getInstancia();
					List<TipoConsumo> tipoConsumos = repoTC.list();
					return tipoConsumos;
				}, json());

				patch("/api/tipo_consumo/:pk", (request, response) -> {
                        JSONObject body = new JSONObject(request.body());
                        TipoConsumoRepositorio repoTC = TipoConsumoRepositorio.getInstancia();
                        FactorEmisionRepositorio repoFE = FactorEmisionRepositorio.getInstancia();
                        
                        TipoConsumo tipoConsumo = repoTC.get(Integer.parseInt(request.params(":pk")));

                        Float nuevoValor = body.getFloat("valor");
                       
                        FactorEmision factorEmision = tipoConsumo.getFactorEmision();
                        factorEmision.setValor(nuevoValor);
                        factorEmision = repoFE.save(factorEmision);                    
                        response.status(203);
                        return factorEmision;
                });

                post("/cargar_params_sistema/", (req, res) -> {
                        Boolean error = false;
                        String errorStr = "";
                        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                        try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
                                // Use the input stream to create a file
                                File file = new File("tempFile2.csv");
                                ControladorBatchMedicion.copyInputStreamToFile(is, file);
                                ParserCSV parser = new ParserCSV("tempFile2.csv");

                                new Organizacion().cargarParametros(parser.leerParametrosSistema());
                        } catch (Exception e) {
                                System.out.println("============================\n\n\n\n" + e);

                                error = true;
                        }
                        return ":D";
                });
                post("/reset_server/", (req, res) -> {
                        if (req.queryParams("seguro").equals("si")) {
                                System.out.println("SE VA A RESETEAR EL SERVIDOR!");
                                reset_server();
                        }
                        return ":D";
                });
        }

        private static void reset_server() throws IOException {
                Map<Class, RepositorioBase> repos = new LinkedHashMap<Class, RepositorioBase>() {
                        {
                            put(BatchMedicion.class, BatchMedicionRepositorio.getInstancia());

                            put(Medible.class, MedibleRepositorio.getInstancia());
                            put(Medicion.class, MedicionRepositorio.getInstancia());
                            put(TipoOrganizacion.class, TipoOrganizacionRepositorio.getInstancia());
                            put(Organizacion.class, OrganizacionRepositorio.getInstancia());
                            put(Agente.class, AgenteRepositorio.getInstancia());
                            put(MiembroPorSector.class, MiembroPorSectorRepositorio.getInstancia());
                            put(Miembro.class, MiembroRepositorio.getInstancia());
                            put(Sector.class, SectorRepositorio.getInstancia());
                            put(Parada.class, ParadaRepositorio.getInstancia());
                            put(SectorTerritorial.class, SectorTerritorialRepositorio.getInstancia());
                            put(TipoTransporte.class, TipoTransporteRepositorio.getInstancia());
                            put(TransportePublico.class, TransportePublicoRepositorio.getInstancia());
                            put(TransporteEcologico.class, TransporteEcologicoRepositorio.getInstancia());
                            put(TransporteParticular.class, TransporteParticularRepositorio.getInstancia());
                            put(MedioDeTransporte.class, MedioDeTransporteRepositorio.getInstancia());
                            put(Tramo.class, TramoRepositorio.getInstancia());
                            put(Trayecto.class, TrayectoRepositorio.getInstancia());
                            put(Consumo.class, ConsumoRepositorio.getInstancia());
                            put(TipoConsumo.class, TipoConsumoRepositorio.getInstancia());
                            put(FactorEmision.class, FactorEmisionRepositorio.getInstancia());
                            put(Actividad.class, ActividadRepositorio.getInstancia());
                            put(Unidad.class, UnidadRepositorio.getInstancia());
                            put(Ubicacion.class, UbicacionRepositorio.getInstancia());
                            put(PeriodoDeImputacion.class, PeriodoDeImputacionRepositorio.getInstancia());
                            put(Usuario.class, UsuarioRepositorio.getInstancia());
                        }
                };

          
                EntityManagerHelper.beginTransaction();
                EntityManagerHelper.getEntityManager()
                .createNativeQuery("SET session_replication_role = 'replica';").executeUpdate();
                EntityManagerHelper.commit();
                
                // postgres:
                // SET session_replication_role = 'replica';
                // SET session_replication_role = 'origin';

                // mysql:
                // SET FOREIGN_KEY_CHECKS = 0;
                // SET FOREIGN_KEY_CHECKS = 1;

                for (Map.Entry<Class, RepositorioBase> set : repos.entrySet()) {
                    System.out.println(set.getKey().getName());
                    set.getValue().truncate();
                    
                }
                EntityManagerHelper.beginTransaction();
                EntityManagerHelper.getEntityManager()
                .createNativeQuery("SET session_replication_role = 'origin';").executeUpdate();
                EntityManagerHelper.commit();
                
                List<Parada> paradas107 = new ArrayList<>();
                List<Parada> paradas145 = new ArrayList<>();
                List<Tramo> tramos = new ArrayList<>();
                List<Tramo> tramosVuelta = new ArrayList<>();
                List<Tramo> tramosMaxi = new ArrayList<>();
                List<Tramo> tramosCeci = new ArrayList<>();
                List<Tramo> tramosFranBondi = new ArrayList<>();
                List<Tramo> tramosFranAPie = new ArrayList<>();
                List<Tramo> tramosYayiAuto = new ArrayList<>();
                List<Tramo> tramosYayiBondi1 = new ArrayList<>();
                List<Tramo> tramosYayiBondi2 = new ArrayList<>();

                List<Tramo> tramosBondiYayiFran = new ArrayList<>();

                List<Trayecto> trayectos = new ArrayList<>();
                List<Trayecto> trayectosMaxi = new ArrayList<>();
                List<Trayecto> trayectosCeci = new ArrayList<>();
                List<Trayecto> trayectosFran = new ArrayList<>();
                List<Trayecto> trayectosYayi = new ArrayList<>();
                List<Trayecto> trayectosVacios = new ArrayList<>();
                Adaptador adapter = new ServicioGeoref();

                List<Medible> medibleCocaCola = new ArrayList<>();
                List<Medible> medibleApple = new ArrayList<>();
                List<Medible> medibleNike = new ArrayList<>();

                List<Sector> sectoresCocaCola = new ArrayList<>();
                List<Sector> sectoresApple = new ArrayList<>();
                List<Sector> sectoresNike = new ArrayList<>();

                // Declaro las listas de los miembros para desp agregar a los miembros y
                // agregarlos a los sectores
                List<Miembro> miembrosMarketingCocaCola = new ArrayList<>();
                List<Miembro> miembrosTecnologiaCocaCola = new ArrayList<>();
                List<Miembro> miembrosManufacturaCocaCola = new ArrayList<>();
                List<Miembro> miembrosMarketingApple = new ArrayList<>();
                List<Miembro> miembrosTecnologiaApple = new ArrayList<>();
                List<Miembro> miembrosManufacturaApple = new ArrayList<>();
                List<Miembro> miembrosMarketingNike = new ArrayList<>();
                List<Miembro> miembrosTecnologiaNike = new ArrayList<>();
                List<Miembro> miembrosManufacturaNike = new ArrayList<>();

                repos.get(Actividad.class).save(new Actividad("Traslado de Miembros de la Organizacion", Alcance.EMISION_INDIRECTA));

                Unidad uTC = new Unidad("KM");
                uTC = (Unidad) repos.get(Unidad.class).save(uTC);
                Unidad uFE = new Unidad("KGCO2EQ", uTC);
                uFE = (Unidad) repos.get(Unidad.class).save(uFE);
                FactorEmision fe = new FactorEmision(3.0, uFE);
                repos.get(FactorEmision.class).save(fe);
                repos.get(TipoConsumo.class).save(new TipoConsumo(uTC, fe,"Distancia media recorrida"));
                
                
                TipoOrganizacion empresa = new TipoOrganizacion("Empresa SA");
                empresa = (TipoOrganizacion) repos.get(TipoOrganizacion.class).save(empresa);

                Ubicacion lomasDeZamora = new Ubicacion("Lomas De Zamora", "600", 400f, 500f, "Jose Marmol",
                                "Buenos Aires",
                                "Almirante Brown"); // Bien
                lomasDeZamora= (Ubicacion) repos.get(Ubicacion.class).save(lomasDeZamora);

                Ubicacion canning = new Ubicacion("Sargento Cabral", "3450", 300f, 400f, "Canning", "Buenos Aires",
                        "Esteban Echeverria"); // Bien
                canning = (Ubicacion) repos.get(Ubicacion.class).save(canning);

                Ubicacion palermo = new Ubicacion("Thames", "400", 300f, 550f, "Pavon", "Buenos Aires",
                        "Exaltacion de la Cruz"); // Bien
                palermo = (Ubicacion) repos.get(Ubicacion.class).save(palermo);

                Ubicacion obelisco = new Ubicacion("Av 9 de Julio", "1000", 700f, 800f, "Nieves", "Buenos Aires",
                        "Azul"); // Bien
                obelisco = (Ubicacion) repos.get(Ubicacion.class).save(obelisco);

                Ubicacion lanus = new Ubicacion("Lanus", "500", 410f, 510f, "San Jose", "Buenos Aires",
                        "Almirante Brown"); // Bien
                lanus = (Ubicacion) repos.get(Ubicacion.class).save(lanus);


                Ubicacion once = new Ubicacion("Once", "200", 100f, 200f, "La Capilla", "Buenos Aires",
                                "Florencio Varela"); // Bien
                once = (Ubicacion) repos.get(Ubicacion.class).save(once);


                Ubicacion villaCrespo = new Ubicacion("Aguirre", "664", 304f, 558f, "Hudson", "Buenos Aires",
                                "Berazategui"); // Bien
                villaCrespo = (Ubicacion) repos.get(Ubicacion.class).save(villaCrespo);

                Ubicacion caballito = new Ubicacion("Caballito", "450", 800f, 100f, "La Union", "Buenos Aires",
                        "Ezeiza"); // Bien
                caballito = (Ubicacion) repos.get(Ubicacion.class).save(caballito);

                Ubicacion paternal = new Ubicacion("Gavilan", "2151", 900f, 250f, "Pereyra", "Buenos Aires",
                                "Berazategui"); // Bien

                paternal = (Ubicacion) repos.get(Ubicacion.class).save(paternal);
                Ubicacion flores = new Ubicacion("Flores", "137", 991f, 280f, "Sarandi", "Buenos Aires", "Avellaneda"); // Bien
                flores = (Ubicacion) repos.get(Ubicacion.class).save(flores);

                // TODO: VER LO DE LA CONEXION ENTRE AGENTE Y ORG

                Organizacion cocaCola = new Organizacion("Coca-Cola", lomasDeZamora, sectoresCocaCola,
                                Clasificacion.SECTOR_PRIMARIO, medibleCocaCola, empresa, "https://logos-world.net/wp-content/uploads/2020/03/Coca-Cola-Logo-700x394.png"); // Bien
                cocaCola = (Organizacion) repos.get(Organizacion.class).save(cocaCola);
                Organizacion apple = new Organizacion("Apple", caballito, sectoresApple, Clasificacion.SECTOR_PRIMARIO,
                                medibleApple, empresa, "https://1000logos.net/wp-content/uploads/2016/10/Apple-Logo-500x281.png"); // Bien
                apple = (Organizacion) repos.get(Organizacion.class).save(apple);
                Organizacion nike = new Organizacion("Nike", once, sectoresNike, Clasificacion.SECTOR_PRIMARIO,
                                medibleNike, empresa, "https://upload.wikimedia.org/wikipedia/commons/3/36/Logo_nike_principal.jpg");// Bien
                nike = (Organizacion) repos.get(Organizacion.class).save(nike);

                Sector marketingCocaCola = new Sector("Marketing Coca Cola", cocaCola); // Bien
                marketingCocaCola = (Sector) repos.get(Sector.class).save(marketingCocaCola);
                Sector tecnologiaCocaCola = new Sector("Tecnologia Coca Cola", cocaCola); // Bien
                tecnologiaCocaCola = (Sector) repos.get(Sector.class).save(tecnologiaCocaCola);
                Sector manufacturaCocaCola = new Sector("Manufactura Coca Cola", cocaCola);// Bien
                manufacturaCocaCola = (Sector) repos.get(Sector.class).save(manufacturaCocaCola);

                Sector marketingApple = new Sector("Marketing Apple", apple);// Bien
                marketingApple = (Sector) repos.get(Sector.class).save(marketingApple);
                Sector tecnologiaApple = new Sector("Tecnologia Apple", apple);// Bien
                tecnologiaApple = (Sector) repos.get(Sector.class).save(tecnologiaApple);
                Sector manufacturaApple = new Sector("Manufactura Apple", apple);// Bien
                manufacturaApple = (Sector) repos.get(Sector.class).save(manufacturaApple);

                Sector marketingNike = new Sector("Marketing Nike", nike);// Bien
                marketingNike = (Sector) repos.get(Sector.class).save(marketingNike);
                Sector tecnologiaNike = new Sector("Tecnologia Nike", nike);// Bien
                tecnologiaNike = (Sector) repos.get(Sector.class).save(tecnologiaNike);
                Sector manufacturaNike = new Sector("Manufactura Nike", nike);// Bien
                manufacturaNike = (Sector) repos.get(Sector.class).save(manufacturaNike);

                // RECORRIDO 107
                Parada paradaLomas = new Parada(lomasDeZamora, 0f, 141.4213f); // Bien
                paradaLomas = (Parada) repos.get(Parada.class).save(paradaLomas);
                paradas107.add(paradaLomas);
                Parada paradaCanning = new Parada(canning, 141.4213f, 150.2011f); // Bien
                paradaCanning = (Parada) repos.get(Parada.class).save(paradaCanning);
                paradas107.add(paradaCanning);
                Parada paradaPalermo = new Parada(palermo, 150.2011f, 471.0842f); // Bien
                paradaPalermo = (Parada)repos.get(Parada.class).save(paradaPalermo);
                paradas107.add(paradaPalermo);
                Parada paradaObelisco = new Parada(obelisco, 471.0842f, 410.1219f); // Bien
                paradaObelisco = (Parada) repos.get(Parada.class).save(paradaObelisco);
                paradas107.add(paradaObelisco);
                Parada paradaLanus = new Parada(lanus, 410.1219f, 0f); // Bien
                paradaLanus = (Parada) repos.get(Parada.class).save(paradaLanus);
                paradas107.add(paradaLanus);

                // RECORRIDO 145
                Parada paradaPalermo2 = new Parada(palermo, 0f, 100.4512f); // Bien
                paradaPalermo2 = (Parada) repos.get(Parada.class).save(paradaPalermo2);
                paradas145.add(paradaPalermo2);
                Parada paradaVillaCrespo = new Parada(villaCrespo, 100.4512f, 181.4213f); // Bien
                paradaVillaCrespo = (Parada) repos.get(Parada.class).save(paradaVillaCrespo);
                paradas145.add(paradaVillaCrespo);
                Parada paradaCaballito = new Parada(caballito, 181.4213f, 150.9000f); // Bien
                paradaCaballito = (Parada) repos.get(Parada.class).save(paradaCaballito);
                paradas145.add(paradaCaballito);
                Parada paradaPaternal = new Parada(paternal, 150.9000f, 210.1215f); // Bien
                paradaPaternal = (Parada) repos.get(Parada.class).save(paradaPaternal);
                paradas145.add(paradaPaternal);
                Parada paradaFlores = new Parada(flores, 210.1215f, 0f); // Bien
                paradaFlores = (Parada) repos.get(Parada.class).save(paradaFlores);
                paradas145.add(paradaFlores);

                TipoTransporte colectivo = new TipoTransporte("Colectivo"); // Bien
                colectivo = (TipoTransporte) repos.get(TipoTransporte.class).save(colectivo);
                TipoTransporte auto = new TipoTransporte("Auto"); // Bien
                auto = (TipoTransporte) repos.get(TipoTransporte.class).save(auto);
                TipoTransporte bicicleta = new TipoTransporte("Bicicleta"); // Bien
                bicicleta = (TipoTransporte) repos.get(TipoTransporte.class).save(bicicleta);
                TipoTransporte aPie = new TipoTransporte("aPie"); // Bien
                aPie = (TipoTransporte) repos.get(TipoTransporte.class).save(aPie);

                TransporteEcologico bici = new TransporteEcologico(bicicleta, "Bicicleta");// Bien
                bici = (TransporteEcologico) repos.get(TransporteEcologico.class).save(bici);
                TransporteEcologico caminando = new TransporteEcologico(aPie, "Caminando");// Bien
                caminando = (TransporteEcologico) repos.get(MedioDeTransporte.class).save(caminando);
                TransportePublico colectivo107 = new TransportePublico(colectivo, paradas107, "107", "Colectivo 107");// Bien
                colectivo107 = (TransportePublico) repos.get(MedioDeTransporte.class).save(colectivo107);

                paradaLomas.setTransportePublico(colectivo107);
                paradaCanning.setTransportePublico(colectivo107);
                paradaPalermo.setTransportePublico(colectivo107);
                paradaObelisco.setTransportePublico(colectivo107);
                paradaLanus.setTransportePublico(colectivo107);

                TransportePublico colectivo145 = new TransportePublico(colectivo, paradas145, "145", "Colectivo 145");// Bien
                colectivo145 = (TransportePublico) repos.get(TransportePublico.class).save(colectivo145);

                paradaPalermo2.setTransportePublico(colectivo145);
                paradaVillaCrespo.setTransportePublico(colectivo145);
                paradaCaballito.setTransportePublico(colectivo145);
                paradaPaternal.setTransportePublico(colectivo145);
                paradaFlores.setTransportePublico(colectivo145);

                TransporteParticular ferrari = new TransporteParticular(auto, "NAFTA", "Ferrari V"); // Bien
                ferrari = (TransporteParticular) repos.get(TransporteParticular.class).save(ferrari);
                TransporteParticular fiat600 = new TransporteParticular(auto, "NAFTA", "Fiat 600"); // Bien
                fiat600 = (TransporteParticular) repos.get(TransporteParticular.class).save(fiat600);

                // CASO SOLO BICI
                Tramo tramoMaxi = new Tramo(lomasDeZamora, palermo); // distancia 291,6225546
                tramoMaxi = (Tramo) repos.get(Tramo.class).save(tramoMaxi);
                tramosMaxi.add(tramoMaxi);
                Trayecto trayectoMaxi = new Trayecto(lomasDeZamora, palermo, bici, tramosMaxi, 1);
                trayectoMaxi = (Trayecto) repos.get(Trayecto.class).save(trayectoMaxi);
                trayectosMaxi.add(trayectoMaxi);
                tramoMaxi.setTrayecto(trayectoMaxi); // Bien

                // CASO SOLO VEHICULO PARTICULAR
                Tramo tramoCeci = new Tramo(canning, caballito); //// distancia = 150.2011984
                tramoCeci = (Tramo) repos.get(Tramo.class).save(tramoCeci);
                tramosCeci.add(tramoCeci);
                Trayecto trayectoCeci = new Trayecto(canning, caballito, ferrari, tramosCeci, 1);
                trayectoCeci = (Trayecto) repos.get(Trayecto.class).save(trayectoCeci);
                trayectosCeci.add(trayectoCeci);
                trayectosYayi.add(trayectoCeci);
                tramoCeci.setTrayecto(trayectoCeci); // Bien



                // CASO COLECTIVO Y CAMINANDO
                Tramo tramoBondi145 = new Tramo(villaCrespo, caballito); //// distancia = 181.4213562
                tramoBondi145 = (Tramo) repos.get(Tramo.class).save(tramoBondi145);
                tramosBondiYayiFran.add(tramoBondi145);
                Tramo tramoFran2 = new Tramo(caballito, paternal);//// distancia = 150.9000980
                tramoFran2 = (Tramo) repos.get(Tramo.class).save(tramoFran2);
                tramosFranAPie.add(tramoFran2);
                Trayecto trayectoCompartido = new Trayecto(villaCrespo, caballito, colectivo145, tramosBondiYayiFran, 2); // CREO EL TRAYECTO COMPARTIDO ENTRE YAYI y FRAN
                trayectoCompartido = (Trayecto) repos.get(Trayecto.class).save(trayectoCompartido);

                Trayecto trayectoFranAPie = new Trayecto(caballito, paternal, caminando, tramosFranAPie, 1);
                trayectoFranAPie = (Trayecto) repos.get(Trayecto.class).save(trayectoFranAPie);
                trayectosFran.add(trayectoCompartido);
                trayectosFran.add(trayectoFranAPie);
                tramoBondi145.setTrayecto(trayectoCompartido);// Bien
                tramoFran2.setTrayecto(trayectoFranAPie);// Bien

                // CASO AUTO Y COLECTIVO
                Tramo tramoFiat1 = new Tramo(lomasDeZamora, canning); // distancia = 141.4213562
                tramoFiat1 = (Tramo) repos.get(Tramo.class).save(tramoFiat1);
                Tramo tramoFiat2 = new Tramo(canning, lomasDeZamora, caballito); // distancia = 150.2011984
                tramoFiat2 = (Tramo) repos.get(Tramo.class).save(tramoFiat2);
                tramosYayiAuto.add(tramoFiat1);
                tramosYayiAuto.add(tramoFiat2);
                Trayecto trayectoYayiAuto = new Trayecto(lomasDeZamora, palermo, fiat600, tramosYayiAuto, 1);
                trayectoYayiAuto = (Trayecto) repos.get(Trayecto.class).save(trayectoYayiAuto);
                trayectosYayi.add(trayectoYayiAuto);
                tramoFiat1.setTrayecto(trayectoYayiAuto);// Bien
                tramoFiat2.setTrayecto(trayectoYayiAuto);// Bien

                // Bondi 145 hasta la otra organizacion ++ comparte trayecto con FRANCO
                Tramo tramoBondiYayi1 = new Tramo(palermo, villaCrespo); // distancia = 100.4512456
                tramoBondiYayi1 = (Tramo) repos.get(Tramo.class).save(tramoBondiYayi1);
                tramosYayiBondi1.add(tramoBondiYayi1);
                Trayecto trayectoYayiBondi1 = new Trayecto(palermo, villaCrespo, colectivo145, tramosYayiBondi1, 1);
                trayectoYayiBondi1 = (Trayecto) repos.get(Trayecto.class).save(trayectoYayiBondi1);
                tramoBondiYayi1.setTrayecto(trayectoYayiBondi1); // Bien

                // ACA LA PARTE QUE COMPARTE CON FRAN ENTRE VILLA CRESPO Y CABALLITO

                Tramo tramoBondiYayi2 = new Tramo(caballito, paternal); // distancia = 150.9000980
                tramoBondiYayi2 = (Tramo) repos.get(Tramo.class).save(tramoBondiYayi2);
                tramosYayiBondi2.add(tramoBondiYayi2);
                Trayecto trayectoYayiBondi2 = new Trayecto(caballito, paternal, colectivo145, tramosYayiBondi2, 1);
                trayectoYayiBondi2 = (Trayecto) repos.get(Trayecto.class).save(trayectoYayiBondi2);
                trayectosYayi.add(trayectoYayiBondi1);
                trayectosYayi.add(trayectoCompartido);
                trayectosYayi.add(trayectoYayiBondi2);
                tramoBondiYayi2.setTrayecto(trayectoYayiBondi2);// Bien

                // Miembros Coca
                // marketing
                Miembro julian = new Miembro("Julian", "Mesa", TipoDni.DNI, 41588959L, trayectosVacios);
                julian = (Miembro) repos.get(Miembro.class).save(julian);
                Miembro diego = new Miembro("Diego", "Maradona", TipoDni.DNI, 15565002L, trayectos);
                diego = (Miembro) repos.get(Miembro.class).save(diego);
                Miembro yasmin = new Miembro("Yasmin", "Elias", TipoDni.DNI, 43456783L, trayectosYayi);
                yasmin = (Miembro) repos.get(Miembro.class).save(yasmin);

                // Agrego los miembros a la lista de miembros de marketing de coca cola
                miembrosMarketingCocaCola.add(julian);
                miembrosMarketingCocaCola.add(diego);
                miembrosMarketingCocaCola.add(yasmin);

                // Creo los miembrosPorSector
                MiembroPorSector julianMarketingCocaCola = new MiembroPorSector(julian, marketingCocaCola, true);
                julianMarketingCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(julianMarketingCocaCola);
                MiembroPorSector diegoMarketingCocaCola = new MiembroPorSector(diego, marketingCocaCola, true);
                diegoMarketingCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(diegoMarketingCocaCola);
                MiembroPorSector yasminMarketingCocaCola = new MiembroPorSector(yasmin, marketingCocaCola, true);
                yasminMarketingCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(yasminMarketingCocaCola);

                // Seteo la lista de miembros al sector
                marketingCocaCola.setMiembrosAceptados(miembrosMarketingCocaCola);

                // tecnologia
                Miembro maximiliano = new Miembro("Maximiliano", "De la Fuente", TipoDni.DNI, 41672222L, trayectosMaxi);
                maximiliano = (Miembro) repos.get(Miembro.class).save(maximiliano);
                Miembro cecilia = new Miembro("Cecilia", "Roca", TipoDni.DNI, 41588950L, trayectosCeci);
                cecilia = (Miembro) repos.get(Miembro.class).save(cecilia);
                Miembro ezequiel = new Miembro("Ezequiel", "Sosa", TipoDni.DNI, 34565782L, trayectosVacios);
                ezequiel = (Miembro) repos.get(Miembro.class).save(ezequiel);

                // Agrego los miembros a la lista de miembros de tecnologia de coca cola
                miembrosTecnologiaCocaCola.add(cecilia);
                miembrosTecnologiaCocaCola.add(maximiliano);
                miembrosTecnologiaCocaCola.add(ezequiel);

                // Creo los miembrosPorSector
                MiembroPorSector maximilianoTecnologiaCocaCola = new MiembroPorSector(maximiliano, tecnologiaCocaCola, true);
                maximilianoTecnologiaCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(maximilianoTecnologiaCocaCola);
                MiembroPorSector ceciliaTecnologiaCocaCola = new MiembroPorSector(cecilia, tecnologiaCocaCola, true);
                ceciliaTecnologiaCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(ceciliaTecnologiaCocaCola);
                MiembroPorSector ezequielTecnologiaCocaCola = new MiembroPorSector(ezequiel, tecnologiaCocaCola, true);
                ezequielTecnologiaCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(ezequielTecnologiaCocaCola);

                // Seteo la lista de miembros al sector
                tecnologiaCocaCola.setMiembrosAceptados(miembrosTecnologiaCocaCola);

                // manufactura
                Miembro nicolas = new Miembro("Nicolas", "Ramos", TipoDni.DNI, 43777832L, trayectosVacios);
                nicolas = (Miembro) repos.get(Miembro.class).save(nicolas);
                Miembro sebastian = new Miembro("Sebastian", "De la Fuente", TipoDni.DNI, 21672022L, trayectos);
                sebastian = (Miembro) repos.get(Miembro.class).save(sebastian);
                Miembro erica = new Miembro("Erica", "Rosas", TipoDni.DNI, 15588909L, trayectos);
                erica = (Miembro) repos.get(Miembro.class).save(erica);

                // Agrego los miembros a la lista de miembros de manufactura de coca cola
                miembrosManufacturaCocaCola.add(nicolas);
                miembrosManufacturaCocaCola.add(sebastian);
                miembrosManufacturaCocaCola.add(erica);

                // Creo los miembrosPorSector
                MiembroPorSector nicolasManufacturaCocaCola = new MiembroPorSector(nicolas, manufacturaCocaCola, true);
                nicolasManufacturaCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(nicolasManufacturaCocaCola);
                MiembroPorSector sebastianManufacturaCocaCola = new MiembroPorSector(sebastian, manufacturaCocaCola, true);
                sebastianManufacturaCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(sebastianManufacturaCocaCola);
                MiembroPorSector ericaManufacturaCocaCola = new MiembroPorSector(erica, manufacturaCocaCola, true);
                ericaManufacturaCocaCola = (MiembroPorSector) repos.get(MiembroPorSector.class).save(ericaManufacturaCocaCola);

                // Seteo la lista de miembros al sector
                manufacturaCocaCola.setMiembrosAceptados(miembrosManufacturaCocaCola);

                // Seteo los sectores a la lista de sectores de coca cola
                sectoresCocaCola.add(marketingCocaCola);
                sectoresCocaCola.add(tecnologiaCocaCola);
                sectoresCocaCola.add(manufacturaCocaCola);

                // Seteo la lista de sectores a la org coca cola
                cocaCola.setSectores(sectoresCocaCola);

                // Miembros Apple
                // marketing
                Miembro franco = new Miembro("Franco", "Scolari", TipoDni.DNI, 42025156L, trayectosFran);
                franco = (Miembro) repos.get(Miembro.class).save(franco);
                Miembro ramiro = new Miembro("Ramiro", "Funes", TipoDni.DNI, 35799932L, trayectos);
                ramiro = (Miembro) repos.get(Miembro.class).save(ramiro);
                Miembro santiago = new Miembro("Santiago", "Del Moro", TipoDni.DNI, 20672122L, trayectos);
                santiago = (Miembro) repos.get(Miembro.class).save(santiago);

                // Agrego los miembros a la lista de miembros de marketing de Apple
                miembrosMarketingApple.add(franco);
                miembrosMarketingApple.add(ramiro);
                miembrosMarketingApple.add(santiago);

                // Creo los miembrosPorSector
                MiembroPorSector francoMarketingApple = new MiembroPorSector(franco, marketingApple, true);
                francoMarketingApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(francoMarketingApple);
                MiembroPorSector ramiroMarketingApple = new MiembroPorSector(ramiro, marketingApple, true);
                ramiroMarketingApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(ramiroMarketingApple);
                MiembroPorSector santiagoMarketingApple = new MiembroPorSector(santiago, marketingApple, true);
                santiagoMarketingApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(santiagoMarketingApple);

                // Seteo la lista de miembros al sector
                marketingApple.setMiembrosAceptados(miembrosMarketingApple);

                // tecnologia
                Miembro rodrigo = new Miembro("Rodrigo", "Bueno", TipoDni.DNI, 11500909L, trayectos);
                rodrigo = (Miembro) repos.get(Miembro.class).save(rodrigo);
                Miembro carla = new Miembro("Carla", "Martinez", TipoDni.DNI, 19565112L, trayectos);
                carla = (Miembro) repos.get(Miembro.class).save(carla);
                Miembro roberto = new Miembro("Roberto", "de Niro", TipoDni.DNI, 10709930L, trayectos);
                roberto = (Miembro) repos.get(Miembro.class).save(roberto);

                // EN ESTE SECTOR TAMBIEN ESTRARIA YASMIN, PERTENECE A AMBAS ORGANIZACIONES
                // Agrego los miembros a la lista de miembros de tecnologia de Apple
                miembrosTecnologiaApple.add(rodrigo);
                miembrosTecnologiaApple.add(carla);
                miembrosTecnologiaApple.add(roberto);
                miembrosTecnologiaApple.add(yasmin);

                // Creo los miembrosPorSector
                MiembroPorSector rodrigoTecnologiaApple = new MiembroPorSector(rodrigo, tecnologiaApple, true);
                rodrigoTecnologiaApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(rodrigoTecnologiaApple);
                MiembroPorSector carlaTecnologiaApple = new MiembroPorSector(carla, tecnologiaApple, true);
                carlaTecnologiaApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(carlaTecnologiaApple);
                MiembroPorSector robertoTecnologiaApple = new MiembroPorSector(roberto, tecnologiaApple, true);
                robertoTecnologiaApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(robertoTecnologiaApple);
                MiembroPorSector yasminTecnologiaApple = new MiembroPorSector(yasmin, tecnologiaApple, true);
                yasminTecnologiaApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(yasminTecnologiaApple);

                // Seteo la lista de miembros al sector
                tecnologiaApple.setMiembrosAceptados(miembrosTecnologiaApple);

                // manufactura
                Miembro leonardo = new Miembro("Leonardo", "Del Caño", TipoDni.DNI, 23611122L, trayectos);
                leonardo = (Miembro) repos.get(Miembro.class).save(leonardo);
                Miembro martina = new Miembro("Martina", "Rodriguez", TipoDni.DNI, 19500000L, trayectos);
                 martina = (Miembro) repos.get(Miembro.class).save(martina);
                Miembro sonia = new Miembro("Sonia", "Martinez", TipoDni.DNI, 13513113L, trayectos);
                sonia = (Miembro) repos.get(Miembro.class).save(sonia);

                // Agrego los miembros a la lista de miembros de manufactura de Apple
                miembrosManufacturaApple.add(leonardo);
                miembrosManufacturaApple.add(martina);
                miembrosManufacturaApple.add(sonia);

                // Creo los miembrosPorSector
                MiembroPorSector leonardoManufacturaApple = new MiembroPorSector(leonardo, manufacturaApple, true);
                leonardoManufacturaApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(leonardoManufacturaApple);
                MiembroPorSector martinaManufacturaApple = new MiembroPorSector(martina, manufacturaApple, true);
                martinaManufacturaApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(martinaManufacturaApple);
                MiembroPorSector soniaManufacturaApple = new MiembroPorSector(sonia, manufacturaApple, true);
                soniaManufacturaApple = (MiembroPorSector) repos.get(MiembroPorSector.class).save(soniaManufacturaApple);

                // Seteo la lista de miembros al sector
                manufacturaApple.setMiembrosAceptados(miembrosManufacturaApple);

                // Seteo los sectores a la lista de sectores de apple
                sectoresApple.add(marketingApple);
                sectoresApple.add(tecnologiaApple);
                sectoresApple.add(manufacturaApple);

                // Seteo la lista de sectores a la org apple
                apple.setSectores(sectoresApple);

                // Miembos Nike
                // marketing
                Miembro daniel = new Miembro("Daniel", "Rios", TipoDni.DNI, 14749934L, trayectos);
                daniel = (Miembro) repos.get(Miembro.class).save(daniel);
                Miembro sofia = new Miembro("Sofia", "Darwin", TipoDni.DNI, 33633322L, trayectos);
                sofia = (Miembro) repos.get(Miembro.class).save(sofia);
                Miembro valentina = new Miembro("Valentina", "Cotto", TipoDni.DNI, 39512345L, trayectos);
                valentina = (Miembro) repos.get(Miembro.class).save(valentina);

                // Agrego los miembros a la lista de miembros de marketing de nike
                miembrosMarketingNike.add(daniel);
                miembrosMarketingNike.add(sofia);
                miembrosMarketingNike.add(valentina);

                // Creo los miembrosPorSector
                MiembroPorSector danielMarketingNike = new MiembroPorSector(daniel, marketingNike, true);
                danielMarketingNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(danielMarketingNike);
                MiembroPorSector sofiaMarketingNike = new MiembroPorSector(sofia, marketingNike, true);
                sofiaMarketingNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(sofiaMarketingNike);
                MiembroPorSector valentinaMarketingNike = new MiembroPorSector(valentina, marketingNike, true);
                valentinaMarketingNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(valentinaMarketingNike);

                // Seteo la lista de miembros al sector
                marketingNike.setMiembrosAceptados(miembrosMarketingNike);

                // tecnologia
                Miembro sabrina = new Miembro("Sabrina", "Lopez", TipoDni.DNI, 43210123L, trayectos);
                sabrina = (Miembro) repos.get(Miembro.class).save(sabrina);
                Miembro dario = new Miembro("Dario", "Ra", TipoDni.DNI, 28299930L, trayectos);
                dario = (Miembro) repos.get(Miembro.class).save(dario);
                Miembro franca = new Miembro("Franca", "Dalles", TipoDni.DNI, 33607022L, trayectos);
                franca = (Miembro) repos.get(Miembro.class).save(franca);

                // Agrego los miembros a la lista de miembros de tecnologia de nike
                miembrosTecnologiaNike.add(sabrina);
                miembrosTecnologiaNike.add(dario);
                miembrosTecnologiaNike.add(franca);

                // Creo los miembrosPorSector
                MiembroPorSector sabrinaTecnologiaNike = new MiembroPorSector(sabrina, tecnologiaNike, true);
                sabrinaTecnologiaNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(sabrinaTecnologiaNike);
                MiembroPorSector darioTecnologiaNike = new MiembroPorSector(dario, tecnologiaNike, true);
                darioTecnologiaNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(darioTecnologiaNike);
                MiembroPorSector francaTecnologiaNike = new MiembroPorSector(franca, tecnologiaNike, true);
                francaTecnologiaNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(francaTecnologiaNike);

                // Seteo la lista de miembros al sector
                tecnologiaNike.setMiembrosAceptados(miembrosTecnologiaNike);

                // manufactura
                Miembro valeria = new Miembro("Valeria", "Carro", TipoDni.DNI, 192132435L, trayectos);
                valeria = (Miembro) repos.get(Miembro.class).save(valeria);
                Miembro juan = new Miembro("Juan", "Perez", TipoDni.DNI, 36636336L, trayectos);
                juan = (Miembro) repos.get(Miembro.class).save(juan);
                Miembro jorge = new Miembro("Jorge", "Perez", TipoDni.DNI, 45012890L, trayectos);
                jorge = (Miembro) repos.get(Miembro.class).save(jorge);

                // EN ESTE SECTOR TAMBIEN ESTRARIA JULIAN, PERTENECE A AMBAS ORGANIZACIONES
                // Agrego los miembros a la lista de miembros de manufactura de nike
                miembrosManufacturaNike.add(valeria);
                miembrosManufacturaNike.add(juan);
                miembrosManufacturaNike.add(jorge);
                miembrosManufacturaNike.add(julian);

                // Creo los miembrosPorSector
                MiembroPorSector valeriaManufacturaNike = new MiembroPorSector(valeria, manufacturaNike, true);
                valeriaManufacturaNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(valeriaManufacturaNike);
                MiembroPorSector juanManufacturaNike = new MiembroPorSector(juan, manufacturaNike, true);
                juanManufacturaNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(juanManufacturaNike);
                MiembroPorSector jorgeManufacturaNike = new MiembroPorSector(jorge, manufacturaNike, true);
                jorgeManufacturaNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(jorgeManufacturaNike);
                MiembroPorSector julianManufacturaNike = new MiembroPorSector(julian, manufacturaNike, true);
                julianManufacturaNike = (MiembroPorSector) repos.get(MiembroPorSector.class).save(julianManufacturaNike);

                // Seteo la lista de miembros al sector
                manufacturaNike.setMiembrosAceptados(miembrosManufacturaNike);

                // Agrego a los sectores a la lista de sectores de nike
                sectoresNike.add(marketingNike);
                sectoresNike.add(tecnologiaNike);
                sectoresNike.add(manufacturaNike);

                // Seteo la lista de sectores a la org de nike
                nike.setSectores(sectoresNike);

                ActividadRepositorio repoActividades = ActividadRepositorio.getInstancia();
                TipoConsumoRepositorio repoTipoConsumo = TipoConsumoRepositorio.getInstancia();

                // UNIDAD - FACTOR EMISION - TIPO CONSUMO- CONSUMO - PERIODO DE IMPUTACION -
                // ACTIVIDAD
                // Combustion Fija,Gas Natural,2.0,ANUAL,2021 --- 2021
                Unidad unidadM3 = new Unidad("M3");
                unidadM3 = (Unidad) repos.get(Unidad.class).save(unidadM3);
                Unidad unidadFEGasNatural = new Unidad("KGCO2EQ", unidadM3);
                unidadFEGasNatural = (Unidad) repos.get(Unidad.class).save(unidadFEGasNatural);
                FactorEmision feGasNatural = new FactorEmision(1, unidadFEGasNatural);
                feGasNatural = (FactorEmision) repos.get(FactorEmision.class).save(feGasNatural);
                TipoConsumo tpGasNatural = new TipoConsumo(unidadM3, feGasNatural, "Gas Natural");
                tpGasNatural = (TipoConsumo) repos.get(TipoConsumo.class).save(tpGasNatural);
                Consumo consumoGasNatural2021 = new Consumo(tpGasNatural, 2.0f, Periodicidad.ANUAL);
                consumoGasNatural2021 = (Consumo) repos.get(Consumo.class).save(consumoGasNatural2021);
                PeriodoDeImputacion periodoMCCANUAL2021 = new PeriodoDeImputacion(Mes.OCTUBRE, 2021);
                periodoMCCANUAL2021 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoMCCANUAL2021);
                Actividad combustionFijaManApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA, manufacturaApple);
                combustionFijaManApple = (Actividad) repos.get(Actividad.class).save(combustionFijaManApple);
                Medicion medGasNaturalManApple = new Medicion(combustionFijaManApple, consumoGasNatural2021, periodoMCCANUAL2021, apple);
                medGasNaturalManApple = (Medicion) repos.get(Medible.class).save(medGasNaturalManApple);

                // Combustion Fija,Gas Natural,3.0,MENSUAL,2022 --- ENERO
                Consumo consumoGasNaturalMCC = new Consumo(tpGasNatural, 3.0f, Periodicidad.MENSUAL);
                consumoGasNaturalMCC = (Consumo) repos.get(Consumo.class).save(consumoGasNaturalMCC);
                PeriodoDeImputacion periodoEnero2022 = new PeriodoDeImputacion(Mes.ENERO, 2022);
                periodoEnero2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoEnero2022);
                Actividad combustionFijaMCC = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA, marketingCocaCola);
                combustionFijaMCC = (Actividad) repos.get(Actividad.class).save(combustionFijaMCC);
                Medicion medGasNaturalCocaCola = new Medicion(combustionFijaMCC, consumoGasNaturalMCC, periodoEnero2022, cocaCola);
                medGasNaturalCocaCola = (Medicion) repos.get(Medible.class).save(medGasNaturalCocaCola);

                // Combustion Movil,Combustible consumido Gasoil,3.0,MENSUAL,2022 --- FEBRERO
                Unidad unidadLTS = new Unidad("LTS");
                unidadLTS = (Unidad) repos.get(Unidad.class).save(unidadLTS);
                Unidad unidadFECombustible = new Unidad("KGCO2EQ", unidadLTS);
                unidadFECombustible = (Unidad) repos.get(Unidad.class).save(unidadFECombustible);
                FactorEmision feCombustibleGasoil = new FactorEmision(3.5, unidadFECombustible);
                feCombustibleGasoil = (FactorEmision) repos.get(FactorEmision.class).save(feCombustibleGasoil);
                TipoConsumo tpCombustibleGasoil = new TipoConsumo(unidadLTS, feCombustibleGasoil, "Combustible consumido Gasoil");
                tpCombustibleGasoil = (TipoConsumo) repos.get(TipoConsumo.class).save(tpCombustibleGasoil);
                Consumo consumoCombustibleGasoilMCC = new Consumo(tpCombustibleGasoil, 3.0f, Periodicidad.MENSUAL);
                consumoCombustibleGasoilMCC = (Consumo) repos.get(Consumo.class).save(consumoCombustibleGasoilMCC);
                PeriodoDeImputacion periodoFebrero2022 = new PeriodoDeImputacion(Mes.FEBRERO, 2022);
                periodoFebrero2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoFebrero2022);
                Actividad combustionMovilMCC1 = new Actividad("Combustion Movil", Alcance.EMISION_DIRECTA, marketingCocaCola);
                combustionMovilMCC1 = (Actividad) repos.get(Actividad.class).save(combustionMovilMCC1);
                Medicion medGasoilCocaCola = new Medicion(combustionMovilMCC1, consumoCombustibleGasoilMCC, periodoFebrero2022, cocaCola);
                medGasoilCocaCola = (Medicion) repos.get(Medible.class).save(medGasoilCocaCola);

                // Combustion Movil,Combustible consumido GNC,5.0,MENSUAL,2022 --- FEBRERO
                FactorEmision feCombustibleGNC = new FactorEmision(2.5, unidadFECombustible);
                feCombustibleGNC = (FactorEmision) repos.get(FactorEmision.class).save(feCombustibleGNC);
                TipoConsumo tpCombustibleGNC = new TipoConsumo(unidadLTS, feCombustibleGNC, "Combustible consumido GNC");
                tpCombustibleGNC = (TipoConsumo) repos.get(TipoConsumo.class).save(tpCombustibleGNC);
                Consumo consumoCombustibleGNCFebreroMApple = new Consumo(tpCombustibleGNC, 5.0f, Periodicidad.MENSUAL);
                consumoCombustibleGNCFebreroMApple = (Consumo) repos.get(Consumo.class).save(consumoCombustibleGNCFebreroMApple);
                Actividad combustionMovilMApple = new Actividad("Combustion Movil", Alcance.EMISION_DIRECTA, marketingApple);
                combustionMovilMApple = (Actividad) repos.get(Actividad.class).save(combustionMovilMApple);
                Medicion medGNCFebreroApple = new Medicion(combustionMovilMApple, consumoCombustibleGNCFebreroMApple, periodoFebrero2022, apple);
                medGNCFebreroApple = (Medicion) repos.get(Medible.class).save(medGNCFebreroApple);

                // Combustion Fija,Fuel Oil,6.0,MENSUAL,2022 ---FEBRERO
                Unidad unidadLT = new Unidad("LT");
                unidadLT = (Unidad) repos.get(Unidad.class).save(unidadLT);
                Unidad unidadFECombus = new Unidad("KGCO2EQ", unidadLT);
                unidadFECombus = (Unidad) repos.get(Unidad.class).save(unidadFECombus);
                FactorEmision feFuelOil = new FactorEmision(5.0, unidadFECombus);
                feFuelOil = (FactorEmision) repos.get(FactorEmision.class).save(feFuelOil);
                TipoConsumo tpFuelOil = new TipoConsumo(unidadLT, feFuelOil, "Fuel Oil");
                tpFuelOil = (TipoConsumo) repos.get(TipoConsumo.class).save(tpFuelOil);
                Consumo consumoFuelOilManNike = new Consumo(tpFuelOil, 6.0f, Periodicidad.MENSUAL);
                consumoFuelOilManNike = (Consumo) repos.get(Consumo.class).save(consumoFuelOilManNike);
                Actividad combustionFijaManNike = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA, manufacturaNike);
                combustionFijaManNike = (Actividad) repos.get(Actividad.class).save(combustionFijaManNike);
                Medicion medFuelOilFebreroNike = new Medicion(combustionFijaManNike, consumoFuelOilManNike, periodoFebrero2022, nike);
                medFuelOilFebreroNike = (Medicion) repos.get(Medible.class).save(medFuelOilFebreroNike);

                // Combustion Fija,Nafta,4.0,MENSUAL,2022 ---ABRIL
                FactorEmision feNafta = new FactorEmision(7.0, unidadFECombus);
                feNafta = (FactorEmision) repos.get(FactorEmision.class).save(feNafta);
                TipoConsumo tpNafta = new TipoConsumo(unidadLT, feNafta, "Nafta");
                tpNafta = (TipoConsumo) repos.get(TipoConsumo.class).save(tpNafta);
                Consumo consumoNaftaTecApple = new Consumo(tpNafta, 4.0f, Periodicidad.MENSUAL);
                consumoNaftaTecApple = (Consumo) repos.get(Consumo.class).save(consumoNaftaTecApple);
                PeriodoDeImputacion periodoAbril2022 = new PeriodoDeImputacion(Mes.ABRIL, 2022);
                periodoAbril2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoAbril2022);
                Actividad combustionFijaAbrilTecApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA, tecnologiaApple);
                combustionFijaAbrilTecApple = (Actividad) repos.get(Actividad.class).save(combustionFijaAbrilTecApple);
                Medicion medNaftaAbrilApple = new Medicion(combustionFijaAbrilTecApple, consumoNaftaTecApple, periodoAbril2022, apple);
                medNaftaAbrilApple = (Medicion) repos.get(Medible.class).save(medNaftaAbrilApple);

                // Combustion movil,Combustible consumido GNC,4.0,ANUAL,2022 ---2022
                Consumo consumoCombustibleGNCTecApple = new Consumo(tpCombustibleGNC, 4.0f, Periodicidad.ANUAL);
                consumoCombustibleGNCTecApple = (Consumo) repos.get(Consumo.class).save(consumoCombustibleGNCTecApple);
                PeriodoDeImputacion periodoANUAL2022 = new PeriodoDeImputacion(Mes.DICIEMBRE, 2022);
                periodoANUAL2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoANUAL2022);
                Actividad combustionMovilTecApple = new Actividad("Combustion Movil", Alcance.EMISION_DIRECTA,
                                tecnologiaApple);
                combustionMovilTecApple = (Actividad) repos.get(Actividad.class).save(combustionMovilTecApple);
                Medicion medAnualGNCApple = new Medicion(combustionMovilTecApple, consumoCombustibleGNCTecApple,
                                periodoANUAL2022, apple);
                medAnualGNCApple = (Medicion) repos.get(Medible.class).save(medAnualGNCApple);

                // Electricidad adquirida y consumida,Electricidad,0.5,MENSUAL,2022 ---ENERO
                Unidad unidadKwh = new Unidad("KWH");
                unidadKwh = (Unidad) repos.get(Unidad.class).save(unidadKwh);
                Unidad unidadFEElectricidad = new Unidad("KGCO2EQ", unidadKwh);
                unidadFEElectricidad = (Unidad) repos.get(Unidad.class).save(unidadFEElectricidad);
                FactorEmision feElectricidad = new FactorEmision(0.5, unidadFEElectricidad);
                feElectricidad = (FactorEmision) repos.get(FactorEmision.class).save(feElectricidad);
                TipoConsumo tpElectricidad = new TipoConsumo(unidadKwh, feElectricidad, "Electricidad");
                tpElectricidad = (TipoConsumo) repos.get(TipoConsumo.class).save(tpElectricidad);
                Consumo consumoElectricoMApple = new Consumo(tpElectricidad, 2.5f, Periodicidad.MENSUAL);
                consumoElectricoMApple = (Consumo) repos.get(Consumo.class).save(consumoElectricoMApple);
                Actividad electricidadMApple = new Actividad("Electricidad adquirida y consumida",
                                Alcance.EMISION_INDIRECTA, marketingApple);
                electricidadMApple = (Actividad) repos.get(Actividad.class).save(electricidadMApple);
                Medicion medElectricidadEneroApple = new Medicion(electricidadMApple, consumoElectricoMApple,
                                periodoEnero2022, apple);
                medElectricidadEneroApple = (Medicion) repos.get(Medible.class).save(medElectricidadEneroApple);

                // Combustion Movil,Combustible consumido Nafta,7.0,MENSUAL,2022 --- FEBRERO
                FactorEmision feCombustibleNafta = new FactorEmision(6.5, unidadFECombustible);
                feCombustibleNafta = (FactorEmision) repos.get(FactorEmision.class).save(feCombustibleNafta);
                TipoConsumo tpCombustibleNafta = new TipoConsumo(unidadLTS, feCombustibleNafta,
                                "Combustible consumido Nafta");
                tpCombustibleNafta = (TipoConsumo) repos.get(TipoConsumo.class).save(tpCombustibleNafta);
                Consumo consumoCombustibleNaftaMApple = new Consumo(tpCombustibleNafta, 7.0f, Periodicidad.MENSUAL);
                consumoCombustibleNaftaMApple = (Consumo) repos.get(Consumo.class).save(consumoCombustibleNaftaMApple);
                Actividad combustionMovilFebreroMApple = new Actividad("Combustion Movil", Alcance.EMISION_DIRECTA,
                                marketingApple);
                combustionMovilFebreroMApple = (Actividad) repos.get(Actividad.class).save(combustionMovilFebreroMApple);
                Medicion medNaftaFebreroApple = new Medicion(combustionMovilFebreroMApple,
                                consumoCombustibleNaftaMApple, periodoFebrero2022, apple);
                medNaftaFebreroApple = (Medicion) repos.get(Medible.class).save(medNaftaFebreroApple);

                // Combustion Fija,Carbon,2.5,MENSUAL,2022 ---MARZO
                Unidad unidadKG = new Unidad("KG");
                unidadKG = (Unidad) repos.get(Unidad.class).save(unidadKG);
                Unidad unidadFECarbon = new Unidad("KGCO2EQ", unidadKG);
                unidadFECarbon = (Unidad) repos.get(Unidad.class).save(unidadFECarbon);
                FactorEmision feCarbon = new FactorEmision(1.5, unidadFECarbon);
                feCarbon = (FactorEmision) repos.get(FactorEmision.class).save(feCarbon);
                TipoConsumo tpCarbon = new TipoConsumo(unidadKG, feCarbon, "Nafta");
                tpCarbon = (TipoConsumo) repos.get(TipoConsumo.class).save(tpCarbon);
                Consumo consumoCarbonManApple = new Consumo(tpCarbon, 2.5f, Periodicidad.MENSUAL);
                consumoCarbonManApple = (Consumo) repos.get(Consumo.class).save(consumoCarbonManApple);
                PeriodoDeImputacion periodoMarzo2022 = new PeriodoDeImputacion(Mes.MARZO, 2021);
                periodoMarzo2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoMarzo2022);
                Actividad combustionFijaMarzoManApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA,
                                manufacturaApple);
                combustionFijaMarzoManApple = (Actividad) repos.get(Actividad.class).save(combustionFijaMarzoManApple);
                Medicion medCarbonMarzoApple = new Medicion(combustionFijaMarzoManApple, consumoCarbonManApple,
                                periodoMarzo2022, apple);
                medCarbonMarzoApple = (Medicion) repos.get(Medible.class).save(medCarbonMarzoApple);

                // Combustion Fija,Nafta,5.0,MENSUAL,2022 ---ABRIL
                Consumo consumoNaftaManApple = new Consumo(tpNafta, 5.0f, Periodicidad.MENSUAL);
                consumoNaftaManApple = (Consumo) repos.get(Consumo.class).save(consumoNaftaManApple);
                Actividad combustionFijaNaftaManApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA,
                                manufacturaApple);
                combustionFijaNaftaManApple = (Actividad) repos.get(Actividad.class).save(combustionFijaNaftaManApple);
                Medicion medCombusNaftaAbrilApple = new Medicion(combustionFijaNaftaManApple, consumoNaftaManApple,
                                periodoAbril2022, apple);
                medCombusNaftaAbrilApple = (Medicion) repos.get(Medible.class).save(medCombusNaftaAbrilApple);

                // Combustion movil,Combustible consumido Gasoil,8.0,ANUAL,2022 ---2022
                Consumo consumoCombustibleGasoilManApple = new Consumo(tpCombustibleGasoil, 8.0f, Periodicidad.ANUAL);
                consumoCombustibleGasoilManApple = (Consumo) repos.get(Consumo.class).save(consumoCombustibleGasoilManApple);
                Actividad combustionMovilManApple = new Actividad("Combustion Movil", Alcance.EMISION_DIRECTA,
                                manufacturaApple);
                combustionMovilManApple = (Actividad) repos.get(Actividad.class).save(combustionMovilManApple);
                Medicion medGasoilAnualApple = new Medicion(combustionMovilManApple, consumoCombustibleGasoilManApple,
                                periodoANUAL2022, apple);
                medGasoilAnualApple = (Medicion) repos.get(Medible.class).save(medGasoilAnualApple);

                // Electricidad adquirida y consumida,Electricidad,3.5,ANUAL,2022
                Consumo consumoTecApple = new Consumo(tpElectricidad, 3.5f, Periodicidad.ANUAL);
                consumoTecApple = (Consumo) repos.get(Consumo.class).save(consumoTecApple);
                Actividad electricidadTecApple = new Actividad("Electricidad adquirida y consumida",
                                Alcance.EMISION_INDIRECTA, tecnologiaApple);
                electricidadTecApple = (Actividad) repos.get(Actividad.class).save(electricidadTecApple);
                Medicion medElectricidadAnualApple = new Medicion(electricidadTecApple, consumoTecApple,
                                periodoANUAL2022, apple);
                medElectricidadAnualApple = (Medicion) repos.get(Medible.class).save(medElectricidadAnualApple);

                // Logística de productos y residuos,Peso total transportado,5.5,MENSUAL,2022
                // --- MAYO
                FactorEmision fePesoTransportado = new FactorEmision(1.0, unidadFECarbon);
                fePesoTransportado = (FactorEmision) repos.get(FactorEmision.class).save(fePesoTransportado);
                TipoConsumo tpPesoTransportado = new TipoConsumo(unidadKG, fePesoTransportado, "Nafta");
                tpPesoTransportado = (TipoConsumo) repos.get(TipoConsumo.class).save(tpPesoTransportado);
                Consumo consumoPesoTransportadoManApple = new Consumo(tpPesoTransportado, 5.5f, Periodicidad.MENSUAL);
                consumoPesoTransportadoManApple = (Consumo) repos.get(Consumo.class).save(consumoPesoTransportadoManApple);
                PeriodoDeImputacion periodoMayo2022 = new PeriodoDeImputacion(Mes.MAYO, 2021);
                periodoMayo2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoMayo2022);
                Actividad logísticaProductosResiduosManApple = new Actividad("Logística de productos y residuos",
                                Alcance.OTRAS_EMISIONES, manufacturaApple);
                logísticaProductosResiduosManApple = (Actividad) repos.get(Actividad.class).save(logísticaProductosResiduosManApple);
                Medicion medLogisticaMayoApple = new Medicion(logísticaProductosResiduosManApple,
                                consumoPesoTransportadoManApple, periodoMayo2022, apple);
                medLogisticaMayoApple = (Medicion) repos.get(Medible.class).save(medLogisticaMayoApple);

                // Combustion Movil,Combustible consumido Nafta,13.0,ANUAL,2022
                Consumo consumoCombustibleNaftaManApple = new Consumo(tpCombustibleNafta, 13.0f, Periodicidad.ANUAL);
                consumoCombustibleNaftaManApple = (Consumo) repos.get(Consumo.class).save(consumoCombustibleNaftaManApple);
                Actividad combustionMovilNaftaManApple = new Actividad("Combustion Movil", Alcance.EMISION_DIRECTA,
                                manufacturaApple);
                combustionMovilNaftaManApple = (Actividad) repos.get(Actividad.class).save(combustionMovilNaftaManApple);
                Medicion medNaftaAnualManApple = new Medicion(combustionMovilNaftaManApple,
                                consumoCombustibleNaftaManApple, periodoANUAL2022, apple);
                medNaftaAnualManApple = (Medicion) repos.get(Medible.class).save(medNaftaAnualManApple);

                // Combustion Fija,Leña,1.5,MENSUAL,2022 ---JUNIO
                FactorEmision feLenia = new FactorEmision(1.5, unidadFECarbon);
                feLenia = (FactorEmision) repos.get(FactorEmision.class).save(feLenia);
                TipoConsumo tpLenia = new TipoConsumo(unidadKG, feLenia, "Leña");
                tpLenia = (TipoConsumo) repos.get(TipoConsumo.class).save(tpLenia);
                Consumo consumoLeniaManApple = new Consumo(tpLenia, 1.5f, Periodicidad.MENSUAL);
                consumoLeniaManApple = (Consumo) repos.get(Consumo.class).save(consumoLeniaManApple);
                PeriodoDeImputacion periodoJunio2022 = new PeriodoDeImputacion(Mes.JUNIO, 2021);
                periodoJunio2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoJunio2022);
                Actividad combustionFijaLeniaManApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA,
                                manufacturaApple);
                combustionFijaLeniaManApple = (Actividad) repos.get(Actividad.class).save(combustionFijaLeniaManApple);
                Medicion medLeniaJunioManApple = new Medicion(combustionFijaLeniaManApple, consumoLeniaManApple,
                                periodoJunio2022, apple);
                medLeniaJunioManApple = (Medicion) repos.get(Medible.class).save(medLeniaJunioManApple);

                // Combustion Fija,Leña,2.0,MENSUAL,2022 -- JUNIO
                Consumo consumoLeniaJunioMApple = new Consumo(tpLenia, 2.0f, Periodicidad.MENSUAL);
                consumoLeniaJunioMApple = (Consumo) repos.get(Consumo.class).save(consumoLeniaJunioMApple);
                Actividad combustionFijaCarbonMApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA,
                                marketingApple);
                combustionFijaCarbonMApple = (Actividad) repos.get(Actividad.class).save(combustionFijaCarbonMApple);
                Medicion medLeniaJunioMApple = new Medicion(combustionFijaCarbonMApple, consumoLeniaJunioMApple,
                                periodoJunio2022, apple);
                medLeniaJunioMApple = (Medicion) repos.get(Medible.class).save(medLeniaJunioMApple);

                // Combustion Fija,Kerosene,4.5,MENSUAL,2022 ----JULIO
                FactorEmision feKerosene = new FactorEmision(3.5, unidadFECombus);
                feKerosene = (FactorEmision) repos.get(FactorEmision.class).save(feKerosene);
                TipoConsumo tpKerosene = new TipoConsumo(unidadLT, feKerosene, "Kerosene");
                tpKerosene = (TipoConsumo) repos.get(TipoConsumo.class).save(tpKerosene);
                Consumo consumoKeroseneManApple = new Consumo(tpKerosene, 4.5f, Periodicidad.MENSUAL);
                consumoKeroseneManApple = (Consumo) repos.get(Consumo.class).save(consumoKeroseneManApple);
                PeriodoDeImputacion periodoJulio2022 = new PeriodoDeImputacion(Mes.JULIO, 2021);
                periodoJulio2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoJulio2022);
                Actividad combustionFijaKeroseneManApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA,
                                manufacturaApple);
                combustionFijaKeroseneManApple = (Actividad) repos.get(Actividad.class).save(combustionFijaKeroseneManApple);
                Medicion medKeroseneJulioManApple = new Medicion(combustionFijaKeroseneManApple,
                                consumoKeroseneManApple, periodoJulio2022, apple);
                medKeroseneJulioManApple = (Medicion) repos.get(Medible.class).save(medKeroseneJulioManApple);

                // Combustion Fija,Diesel,6.0,ANUAL,2022
                FactorEmision feDiesel = new FactorEmision(5.0, unidadFECombus);
                feDiesel = (FactorEmision) repos.get(FactorEmision.class).save(feDiesel);
                TipoConsumo tpDiesel = new TipoConsumo(unidadLT, feKerosene, "Diesel");
                tpDiesel = (TipoConsumo) repos.get(TipoConsumo.class).save(tpDiesel);
                Consumo consumoDieselTecApple = new Consumo(tpDiesel, 6.0f, Periodicidad.MENSUAL);
                consumoDieselTecApple = (Consumo) repos.get(Consumo.class).save(consumoDieselTecApple);
                Actividad combustionFijaDieselTecApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA,
                                tecnologiaApple);
                combustionFijaDieselTecApple = (Actividad) repos.get(Actividad.class).save(combustionFijaDieselTecApple);
                Medicion medDieselAnualManApple = new Medicion(combustionFijaDieselTecApple, consumoDieselTecApple,
                                periodoANUAL2022, apple);
                medDieselAnualManApple = (Medicion) repos.get(Medible.class).save(medDieselAnualManApple);

                // Combustion Fija,Leña,4.5,MENSUAL,2022 ---AGOSTO
                Consumo consumoLeniaAgostoManApple = new Consumo(tpLenia, 4.5f, Periodicidad.MENSUAL);
                consumoLeniaAgostoManApple = (Consumo) repos.get(Consumo.class).save(consumoLeniaAgostoManApple);
                PeriodoDeImputacion periodoAgosto2022 = new PeriodoDeImputacion(Mes.AGOSTO, 2021);
                periodoAgosto2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoAgosto2022);
                Actividad combustionFijaAgostoLeniaManApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA,
                                manufacturaApple);
                combustionFijaAgostoLeniaManApple = (Actividad) repos.get(Actividad.class).save(combustionFijaAgostoLeniaManApple);
                Medicion medLeniaAgostoManApple = new Medicion(combustionFijaAgostoLeniaManApple,
                                consumoLeniaAgostoManApple, periodoAgosto2022, apple);
                medLeniaAgostoManApple = (Medicion) repos.get(Medible.class).save(medLeniaAgostoManApple);

                // Electricidad adquirida y consumida,Electricidad,0.5,MENSUAL,2022
                // ---SEPTIEMBRE
                Consumo consumoElectSeptiemreTecApple = new Consumo(tpElectricidad, 3.5f, Periodicidad.ANUAL);
                consumoElectSeptiemreTecApple = (Consumo) repos.get(Consumo.class).save(consumoElectSeptiemreTecApple);
                PeriodoDeImputacion periodoSeptiembre2022 = new PeriodoDeImputacion(Mes.SEPTIEMBRE, 2021);
                periodoSeptiembre2022 = (PeriodoDeImputacion) repos.get(PeriodoDeImputacion.class).save(periodoSeptiembre2022);
                Actividad electricidadSeptiembreTecApple = new Actividad("Electricidad adquirida y consumida", Alcance.EMISION_INDIRECTA, marketingApple);
                electricidadSeptiembreTecApple = (Actividad) repos.get(Actividad.class).save(electricidadSeptiembreTecApple);
                //Medicion medElectricaAnualTecApple = new Medicion(electricidadSeptiembreTecApple, consumoElectSeptiemreTecApple, periodoSeptiembre2022, apple);
                //medElectricaAnualTecApple = (Medicion) repos.get(Medible.class).save(medElectricaAnualTecApple);

                // Combustion Fija,Fuel Oil,4.5,MENSUAL,2022 ---SEPTIEMBRE
                Consumo consumoFuelOilSeptiembreManApple = new Consumo(tpFuelOil, 6.0f, Periodicidad.MENSUAL);
                consumoFuelOilSeptiembreManApple = (Consumo) repos.get(Consumo.class).save(consumoFuelOilSeptiembreManApple);
                Actividad combustionFijaFOSeptiembreManApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA, marketingApple);
                combustionFijaFOSeptiembreManApple = (Actividad) repos.get(Actividad.class).save(combustionFijaFOSeptiembreManApple);
                //Medicion medFuelOilSeptiembreManApple = new Medicion(combustionFijaFOSeptiembreManApple, consumoFuelOilSeptiembreManApple, periodoSeptiembre2022, apple);
                //medFuelOilSeptiembreManApple = (Medicion) repos.get(Medible.class).save(medFuelOilSeptiembreManApple);

                // Combustion Fija,Kerosene,5.0,MENSUAL,2022 ---SEPTIEMBRE
                Consumo consumoKeroseneSeptiembreManApple = new Consumo(tpKerosene, 5.0f, Periodicidad.MENSUAL);
                consumoKeroseneSeptiembreManApple = (Consumo) repos.get(Consumo.class).save(consumoKeroseneSeptiembreManApple);
                Actividad combustionFijaKeroseneSeptiembreMApple = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA, marketingApple);
                combustionFijaKeroseneSeptiembreMApple = (Actividad) repos.get(Actividad.class).save(combustionFijaKeroseneSeptiembreMApple);
                //Medicion medKeroseneSeptiembreManApple = new Medicion(combustionFijaKeroseneSeptiembreManApple, consumoKeroseneSeptiembreManApple, periodoSeptiembre2022, apple);
                //medKeroseneSeptiembreManApple = (Medicion) repos.get(Medible.class).save(medKeroseneSeptiembreManApple);

                // Combustion Movil,Combustible consumido Nafta,13.0,ANUAL,2022
                Consumo consumoCombustibleNaftaAnaulManApple = new Consumo(tpCombustibleNafta, 13.0f, Periodicidad.ANUAL);
                consumoCombustibleNaftaAnaulManApple = (Consumo) repos.get(Consumo.class).save(consumoCombustibleNaftaAnaulManApple);
                Actividad combustionMovilNaftaAnualMApple = new Actividad("Combustion Movil", Alcance.EMISION_DIRECTA, marketingApple);
                combustionMovilNaftaAnualMApple = (Actividad) repos.get(Actividad.class).save(combustionMovilNaftaAnualMApple);
                //Medicion medNaftaConsumidaAnualManApple = new Medicion(combustionMovilNaftaAnualManApple, consumoCombustibleNaftaAnaulManApple, periodoANUAL2022, apple);
                //medNaftaConsumidaAnualManApple = (Medicion) repos.get(Medible.class).save(medNaftaConsumidaAnualManApple);

                //TODO: JULI, COMENTE ESTAS ULTIMAS 4 MEDICIONES PARA EL CARGAR MEDICIONES DESDE EL ENDOPOINT, ESTOS DATOS SERIAN LOS PARAMETROS NECESARIOS

                medibleApple.add(medGasNaturalManApple);
                medibleApple.add(medGNCFebreroApple);
                medibleApple.add(medNaftaAbrilApple);
                medibleApple.add(medAnualGNCApple);
                medibleApple.add(medElectricidadEneroApple);
                ;
                medibleApple.add(medNaftaFebreroApple);
                medibleApple.add(medCarbonMarzoApple);
                medibleApple.add(medCombusNaftaAbrilApple);
                medibleApple.add(medGasoilAnualApple);
                medibleApple.add(medElectricidadAnualApple);
                medibleApple.add(medLogisticaMayoApple);
                medibleApple.add(medNaftaAnualManApple);
                medibleApple.add(medLeniaJunioManApple);
                ;
                medibleApple.add(medLeniaJunioMApple);
                medibleApple.add(medKeroseneJulioManApple);
                medibleApple.add(medDieselAnualManApple);
                medibleApple.add(medLeniaAgostoManApple);
//                medibleApple.add(medElectricaAnualTecApple);
//                medibleApple.add(medFuelOilSeptiembreManApple);
//                ;
//                medibleApple.add(medKeroseneSeptiembreManApple);
//                medibleApple.add(medNaftaConsumidaAnualManApple);
                apple.setMedibles(medibleApple);

                medibleCocaCola.add(medGasNaturalCocaCola);
                medibleCocaCola.add(medGasoilCocaCola);
                cocaCola.setMedibles(medibleCocaCola);

                medibleNike.add(medFuelOilFebreroNike);
                nike.setMedibles(medibleNike);

                Usuario usuarioYayi = new Usuario("Hola1234", "yayi@hotmail.com", yasmin);
                usuarioYayi = (Usuario) repos.get(Usuario.class).save(usuarioYayi);

                Usuario usuarioJuli= new Usuario("Hola1234", "julian@hotmail.com", julian);
                usuarioJuli = (Usuario) repos.get(Usuario.class).save(usuarioJuli);

                Usuario usuarioMaxi= new Usuario("Hola1234", "maxi@hotmail.com", maximiliano);
                usuarioMaxi = (Usuario) repos.get(Usuario.class).save(usuarioMaxi);

                System.out.println("Arrancamos a guardar los datos en la base");

        }
}
