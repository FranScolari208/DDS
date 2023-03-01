/* package dds2022.grupo1.HuellaDeCarbono;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Adaptador;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.ActividadRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.FactorEmision;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Medicion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Unidad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.TipoOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.Parada;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TipoTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteParticular;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransportePublico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.*;
import dds2022.grupo1.HuellaDeCarbono.services.ServicioGeoref;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObtenerHUTests {

    TipoOrganizacion empresa = new TipoOrganizacion("Empresa SA");

    List<Parada> paradas = new ArrayList<>();
    List<Trayecto> trayectos1 = new ArrayList<>();

    Ubicacion lomasDeZamora = new Ubicacion("Lomas De Zamora 600", 400f,500f);
    Ubicacion lanus = new Ubicacion("Lanus 500", 410f,510f);
    Ubicacion palermo = new Ubicacion("Thames",300.6f,550.2f);
    Ubicacion obelisco = new Ubicacion("Av 9 de Julio 1000", 700f,800f);
    Ubicacion canning = new Ubicacion("Sargento Cabral 3450", 300f, 400f);



    List<Tramo> tramos = new ArrayList<>();
    List<Tramo> tramosVuelta = new ArrayList<>();
    List<Tramo> tramos1 = new ArrayList<>();

    List<Trayecto> trayectos = new ArrayList<>();

    Adaptador adapter = new ServicioGeoref();
    TipoTransporte auto = new TipoTransporte("Auto");
    TipoTransporte colectivo = new TipoTransporte("Colectivo");
    TransporteParticular ferrari = new TransporteParticular(auto, "NAFTA");
    TransportePublico colectivo107 = new TransportePublico (colectivo,paradas,"107");

    Trayecto trayecto = new Trayecto(lomasDeZamora,palermo,colectivo107,tramos,2);
    Trayecto trayecto1 = new Trayecto(lomasDeZamora,lanus,colectivo107,tramos, 2);
    Trayecto trayecto2 = new Trayecto(obelisco,lanus,ferrari,tramos, 2);
    Trayecto trayecto3 = new Trayecto(lomasDeZamora,canning,colectivo107,tramos, 2);
    Trayecto trayecto4 = new Trayecto(canning,lomasDeZamora,colectivo107,tramosVuelta, 2);
    Trayecto trayecto5 = new Trayecto(lomasDeZamora,lanus,colectivo107,tramos1, 2);

    Tramo tramo1 = new Tramo(lomasDeZamora, canning); // distancia = 141.4213562
    Tramo tramo2 = new Tramo(canning, lomasDeZamora, palermo); // distancia = 150.2011984
    Tramo tramo3 = new Tramo(palermo, canning, obelisco); // distancia = 471.0842812
    Tramo tramo4 = new Tramo(obelisco, palermo, lanus); // distancia = 410.1219331
    Tramo tramo5 = new Tramo(canning, lomasDeZamora); // distancia = 141.4213562
    Tramo tramo6 = new Tramo(obelisco, obelisco); // distancia = 0

    Organizacion cocaCola = new Organizacion("Coca-Cola", empresa, lomasDeZamora, Clasificacion.SECTOR_PRIMARIO);
    Miembro julian = new Miembro("juli B)", "Mesa", TipoDni.DNI, 41588959L, trayectos);
    Miembro juliant = new Miembro("juli impostor", "Mesan't", TipoDni.DNI, 41588960L, trayectos);
    Sector marketingCocaCola = new Sector("Marketing CocaCola", cocaCola);

    Actividad combustionFija = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA);

    Unidad unidadKWH = new Unidad("KWH");
    Unidad unidadKM = new Unidad("KM");
    Unidad unidadKG = new Unidad("KGCO2EQ", unidadKWH);
    Unidad unidadTN = new Unidad("TNEC02EQ", unidadKWH);
    FactorEmision factorUno = new FactorEmision(1, unidadKG);
    FactorEmision factorDos = new FactorEmision(10, unidadTN);
    TipoConsumo otroTipoConsumo= new TipoConsumo(unidadKWH,factorDos,"Gasoil");
    TipoConsumo tipoConsumo= new TipoConsumo(unidadKG,factorUno,"Gas Natural");
    TipoConsumo tipoConsumoMal = new TipoConsumo(unidadKM,factorUno,"Gasoil");
    Consumo consumo = new Consumo(tipoConsumo,2.2f, Periodicidad.ANUAL);

    ActividadRepositorio repoActividades = ActividadRepositorio.getInstancia();
    TipoConsumoRepositorio repoTipoConsumo = TipoConsumoRepositorio.getInstancia();



    //Tests req 6 entrega 2
    //Voy a probar ejecutar el calculo no la carga de parametros a traves del parser
    List<Actividad> actividadesTecnologia = new ArrayList<>();
    List<Actividad> actividadesMarketing = new ArrayList<>();

    List<Miembro> miembrosTecnologia = new ArrayList<>();
    List<Miembro> miembrosMarketing = new ArrayList<>();
    List<Miembro> miembrosNoAceptados = new ArrayList<>();

    List<Trayecto> trayectosPepe = new ArrayList();
    List<Trayecto> trayectosMoni = new ArrayList();

    Tramo tramoPepe1 = new Tramo(lomasDeZamora, canning);
    Tramo tramoPepe2 = new Tramo(canning, lomasDeZamora, palermo);
    Tramo tramoPepe3 = new Tramo(palermo, canning, obelisco);

    Trayecto trayectoPepe = new Trayecto(lomasDeZamora,palermo,colectivo107,tramos, 1);
    Trayecto trayectoMoni = new Trayecto(lomasDeZamora,palermo,colectivo107,tramos1, 1);

    Miembro pepe = new Miembro("Pepe", "Argento", TipoDni.DNI, 42025156L, trayectosPepe);
    Miembro moni = new Miembro("Moni", "Argento", TipoDni.DNI, 30567543L, trayectosMoni);

    List<Medible> medibles = new ArrayList();

    Actividad actividad1 = new Actividad("Electricidad adquirida y consumida", Alcance.EMISION_INDIRECTA);
    Actividad actividad2 = new Actividad("Combustion fija", Alcance.EMISION_DIRECTA);

    Unidad unidad1 = new Unidad("KWH");
    Unidad unidad2 = new Unidad("KGCO2EQ", unidad1); //-> KGCO2EQ/kWh (Energia Electrica)
    Unidad unidad3 = new Unidad("KG");
    Unidad unidad4 = new Unidad("KGCO2EQ", unidad3);
    Unidad unidad5 = new Unidad("KM");
    Unidad unidad6 = new Unidad("KGCO2EQ", unidad5);

    FactorEmision factorEmision1 = new FactorEmision(0.5, unidad2); //Factor emision ejemplo de tp 0,5 KGCO2EQ/kWh (Energia Electrica)
    FactorEmision factorEmision2 = new FactorEmision(0.6, unidad4); // Factor emision 0,6 KGCO2EQ/KG (Combustion Fija)
    FactorEmision factorEmision3 = new FactorEmision(0.7, unidad6); // Factor emision 0,7 KGCO2EQ/KM (Distancia Recorrida)

    TipoConsumo tipoConsumo1 = new TipoConsumo(unidad1, factorEmision1, "Electricidad");
    TipoConsumo tipoConsumo2 = new TipoConsumo(unidad3, factorEmision2, "Carbon");
    TipoConsumo tipoConsumo3 = new TipoConsumo(unidad5, factorEmision3, "Distancia media recorrida");

    Consumo consumo1 = new Consumo(tipoConsumo1, 1.5f, Periodicidad.ANUAL);
    Consumo consumo2 = new Consumo(tipoConsumo2, 1.7f, Periodicidad.ANUAL);

    PeriodoDeImputacion periodo1 = new PeriodoDeImputacion(Mes.MAYO, 2020);

    Medicion medicion1 = new Medicion(actividad1, consumo1, periodo1); // -> kWh * KGCO2EQ/kWh => KGCO2EQ
    Medicion medicion2 = new Medicion(actividad2, consumo2, periodo1); // -> KG * KGCO2EQ/KG => KGCO2EQ

    Organizacion apple = new Organizacion("Apple", empresa, lomasDeZamora, Clasificacion.SECTOR_PRIMARIO);

    Sector tecnologia = new Sector("Tecnologia", apple, actividadesTecnologia, miembrosTecnologia, miembrosNoAceptados);
    Sector marketing = new Sector("Marketing", apple, actividadesMarketing, miembrosMarketing, miembrosNoAceptados);
    Parada paradaLomas = new Parada(lomasDeZamora, 0f, 141.4213f);
    Parada paradaCanning = new Parada(canning, 141.4213f, 150.2011f);
    Parada paradaPalermo = new Parada(palermo, 150.2011f, 471.0842f);
    Parada paradaObelisco = new Parada(obelisco, 471.0842f, 410.1219f);
    Parada paradaLanus = new Parada(lanus, 410.1219f, 0f);
    Parada paradaPrueba = new Parada(lomasDeZamora, 0f, 0f);

    @Test
    void calcularHUApple(){
        //calcularHU apple    -> Medibles --> Son los medibles q se pasan por parametro en calcularHU
        //                    -> Sectores ->Tecnologia (Miembros)
        //                                          -> Pepe (Trayectos)
        //                                                  -> TrayectoPepe (Transporte y Tramos)
        //                                                                  -> (lomas-canning, canning-palermo, palermo-obelisco)
        //                                                                  -> Colectivo107 (Paradas)
        //                                                                                  -> (paradaLomas, paradaCanning, paradaPalermo, paradaObelisco, paradaLanus)
        //                                ->Marketing (Miembros)
        //                                          -> Moni (Trayectos)
        //                                                  -> TrayectoMoni (Transporte y Tramos)
        //                                                                  -> (lomas-canning, canning-palermo)
        //                                                                  -> Colectivo107 (Paradas)
        //                                                                                  -> (paradaLomas, paradaCanning, paradaPalermo, paradaObelisco, paradaLanus)

        apple.addSector(tecnologia);
        apple.addSector(marketing);

        //Miembros
        tecnologia.addMiembro(pepe);
        tecnologia.aceptarMiembro(pepe);
        marketing.addMiembro(moni);
        marketing.aceptarMiembro(moni);

        //Paradas del colectivo 107
        paradas.add(paradaLomas);
        paradas.add(paradaCanning);
        paradas.add(paradaPalermo);
        paradas.add(paradaObelisco);
        paradas.add(paradaLanus);

        //Agrego tramos a los tramos de pepe
        tramos.add(tramoPepe1);
        tramos.add(tramoPepe2);
        tramos.add(tramoPepe3);

        //Agrego tramos a los tramos de moni
        tramos1.add(tramoPepe1);
        tramos1.add(tramoPepe2);

        //Agrego trayecto a los trayectos de Pepe
        trayectosPepe.add(trayectoPepe);

        //Agrego trayecto a los trayectos de Moni
        trayectosMoni.add(trayectoMoni);

        //Agrego medibles a la organizacion
        apple.addMedible(medicion1);
        apple.addMedible(medicion2);

        Actividad actividadViaje = ActividadRepositorio.getInstancia().getOrCreateActividad("Traslado de Miembros de la Organizacion", Alcance.OTRAS_EMISIONES);//, Alcance.OTRAS_EMISIONES);
        TipoConsumoRepositorio.getInstancia().addTipoConsumo(tipoConsumo3);

        float huApple = apple.obtenerHU(apple.getMedibles());
        System.out.println(huApple);
        assertEquals(huApple,546887.0f);
    }



    @Test
   void calculoHUMiembro() {

        Actividad actividadViaje = ActividadRepositorio.getInstancia().getOrCreateActividad("Traslado de Miembros de la Organizacion", Alcance.OTRAS_EMISIONES);//, Alcance.OTRAS_EMISIONES);
        TipoConsumoRepositorio.getInstancia().addTipoConsumo(tipoConsumo3);
        //Paradas del colectivo 107
        paradas.add(paradaLomas);
        paradas.add(paradaCanning);
        paradas.add(paradaPalermo);
        paradas.add(paradaObelisco);
        paradas.add(paradaLanus);

        //Agrego tramos a los tramos de pepe
        tramos.add(tramoPepe1);
        tramos.add(tramoPepe2);
        tramos.add(tramoPepe3);

        //Agrego tramos a los tramos de moni
        tramos1.add(tramoPepe1);
        tramos1.add(tramoPepe2);

        //Agrego trayecto a los trayectos de Pepe
        trayectosPepe.add(trayectoPepe);
        System.out.println(pepe.calcularHCtotalMiembro());
        assertEquals(pepe.calcularHCtotalMiembro(), 384404.125);//calcular valor real
    }

    @Test
    void impactoPersonal() throws Exception {
        apple.addSector(tecnologia);
        apple.addSector(marketing);

        //Miembros
        tecnologia.addMiembro(pepe);
        tecnologia.aceptarMiembro(pepe);
        marketing.addMiembro(moni);
        marketing.aceptarMiembro(moni);

        //Paradas del colectivo 107
        paradas.add(paradaLomas);
        paradas.add(paradaCanning);
        paradas.add(paradaPalermo);
        paradas.add(paradaObelisco);
        paradas.add(paradaLanus);

        //Agrego tramos a los tramos de pepe
        tramos.add(tramoPepe1);
        tramos.add(tramoPepe2);
        tramos.add(tramoPepe3);

        //Agrego tramos a los tramos de moni
        tramos1.add(tramoPepe1);
        tramos1.add(tramoPepe2);

        //Agrego trayecto a los trayectos de Pepe
        trayectosPepe.add(trayectoPepe);

        //Agrego trayecto a los trayectos de Moni
        trayectosMoni.add(trayectoMoni);

        //Agrego medibles a la organizacion
        apple.addMedible(medicion1);
        apple.addMedible(medicion2);

        Actividad actividadViaje = ActividadRepositorio.getInstancia().getOrCreateActividad("Traslado de Miembros de la Organizacion", Alcance.OTRAS_EMISIONES);//, Alcance.OTRAS_EMISIONES);
        TipoConsumoRepositorio.getInstancia().addTipoConsumo(tipoConsumo3);

        assertEquals(pepe.calcularMiImpacto(apple), 70.28949737548828f); //aprox 0,29

    }
    @Test
    void impactoPersonalMoni() throws Exception {
        apple.addSector(tecnologia);
        apple.addSector(marketing);

        //Miembros
        tecnologia.addMiembro(pepe);
        tecnologia.aceptarMiembro(pepe);
        marketing.addMiembro(moni);
        marketing.aceptarMiembro(moni);

        //Paradas del colectivo 107
        paradas.add(paradaLomas);
        paradas.add(paradaCanning);
        paradas.add(paradaPalermo);
        paradas.add(paradaObelisco);
        paradas.add(paradaLanus);

        //Agrego tramos a los tramos de pepe
        tramos.add(tramoPepe1);
        tramos.add(tramoPepe2);
        tramos.add(tramoPepe3);

        //Agrego tramos a los tramos de moni
        tramos1.add(tramoPepe1);
        tramos1.add(tramoPepe2);

        //Agrego trayecto a los trayectos de Pepe
        trayectosPepe.add(trayectoPepe);

        //Agrego trayecto a los trayectos de Moni
        trayectosMoni.add(trayectoMoni);

        //Agrego medibles a la organizacion
        apple.addMedible(medicion1);
        apple.addMedible(medicion2);

        Actividad actividadViaje = ActividadRepositorio.getInstancia().getOrCreateActividad("Traslado de Miembros de la Organizacion", Alcance.OTRAS_EMISIONES);//, Alcance.OTRAS_EMISIONES);
        TipoConsumoRepositorio.getInstancia().addTipoConsumo(tipoConsumo3);

        assertEquals(moni.calcularMiImpacto(apple), 26.875326f); //aprox 0,29

    }



}
*/