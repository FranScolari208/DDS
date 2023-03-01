package dds2022.grupo1.HuellaDeCarbono;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Adaptador;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.Parada;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TipoTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteEcologico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteParticular;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransportePublico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.*;
import dds2022.grupo1.HuellaDeCarbono.exceptions.*;
import dds2022.grupo1.HuellaDeCarbono.services.ServicioGeoref;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HuellaDeCarbonoApplicationTests {


	List<Parada> paradas = new ArrayList<>();

	Ubicacion lomasDeZamora1 = new Ubicacion("Lomas De Zamora 600", 400f, 500f);
	Ubicacion lanus = new Ubicacion("Lanus 500", 410f, 510f);
	Ubicacion palermo = new Ubicacion("Thames", 300.6f, 550.2f);
	Ubicacion obelisco = new Ubicacion("Av 9 de Julio 1000", 700f, 800f);
	Ubicacion canning = new Ubicacion("Sargento Cabral 3450", 300f, 400f);

	Parada paradaLomas = new Parada(lomasDeZamora1, 0f, 141.4213f);
	Parada paradaCanning = new Parada(canning, 141.4213f, 150.2011f);
	Parada paradaPalermo = new Parada(palermo, 150.2011f, 471.0842f);
	Parada paradaObelisco = new Parada(obelisco, 471.0842f, 410.1219f);
	Parada paradaLanus = new Parada(lanus, 410.1219f, 0f);
	Parada paradaPrueba = new Parada(obelisco, 0f, 0f);

	List<Tramo> tramos = new ArrayList<>();
	List<Tramo> tramosVuelta = new ArrayList<>();


	List<Trayecto> trayectos = new ArrayList<>();

	TipoTransporte colectivo = new TipoTransporte("Colectivo");
	TipoTransporte auto = new TipoTransporte("Auto");
	TipoTransporte bicicleta = new TipoTransporte("Bicicleta");

	TransporteEcologico bici = new TransporteEcologico(bicicleta);
	TransportePublico colectivo107 = new TransportePublico(colectivo, paradas, "107");

	Adaptador adapter = new ServicioGeoref();

	TransporteParticular ferrari = new TransporteParticular(auto, "NAFTA");

	Trayecto trayecto = new Trayecto(lomasDeZamora1, palermo, colectivo107, tramos, 2);
	Trayecto trayecto1 = new Trayecto(lomasDeZamora1, lanus, colectivo107, tramos, 2);
	Trayecto trayecto2 = new Trayecto(obelisco, lanus, ferrari, tramos, 2);
	Trayecto trayecto3 = new Trayecto(lomasDeZamora1, canning, colectivo107, tramos, 2);
	Trayecto trayecto4 = new Trayecto(canning, lomasDeZamora1, colectivo107, tramosVuelta, 2);

	Tramo tramo1 = new Tramo(lomasDeZamora1, canning); // distancia = 141.4213562
	Tramo tramo2 = new Tramo(canning, lomasDeZamora1, palermo); // distancia = 150.2011984
	Tramo tramo3 = new Tramo(palermo, canning, obelisco); // distancia = 471.0842812
	Tramo tramo4 = new Tramo(obelisco, palermo, lanus); // distancia = 410.1219331
	Tramo tramo5 = new Tramo(canning, lomasDeZamora1); // distancia = 141.4213562
	Tramo tramo6 = new Tramo(obelisco, obelisco); // distancia = 0



	Unidad unidadKWH = new Unidad("KWH");
	Unidad unidadKM = new Unidad("KM");
	Unidad unidadKG = new Unidad("KGCO2EQ", unidadKWH);
	Unidad unidadTN = new Unidad("TNEC02EQ", unidadKWH);
	FactorEmision factorUno = new FactorEmision(1, unidadKG);
	FactorEmision factorDos = new FactorEmision(10, unidadTN);
	TipoConsumo otroTipoConsumo = new TipoConsumo(unidadKWH, factorDos, "Gasoil");
	TipoConsumo tipoConsumo = new TipoConsumo(unidadKG, factorUno, "Gas Natural");
	TipoConsumo tipoConsumoMal = new TipoConsumo(unidadKM, factorUno, "Gasoil");
	Consumo consumo = new Consumo(tipoConsumo, 2.2f, Periodicidad.ANUAL);


// ************************NUEVOS DATOS*********************************************
Ubicacion lomasDeZamora = new Ubicacion("Lomas De Zamora", "600", 400f, 500f, "Glew", "Buenos Aires",
		"Almirante Brown"); // Bien
	Ubicacion caballito = new Ubicacion("Caballito", "450", 800f, 100f, "Luis Guillon", "Buenos Aires",
			"Esteban Echeverria"); // Bien
	Ubicacion once = new Ubicacion("Once", "200", 100f, 200f, "La Union", "Buenos Aires", "Ezeiza"); // Bien

	List<Trayecto> trayectosYayiFran = new ArrayList<>();
	List<Parada> paradas145 = new ArrayList<>();
	List<Sector> sectoresCocaCola = new ArrayList<>();
	List<Tramo> tramosBondiYayiFran = new ArrayList<>();
	List<Miembro> miembrosMarketingCocaCola = new ArrayList<>();
	List<Miembro> miembrosNoAceptadosMarketingCocaCola = new ArrayList<>();
	List<Medible> medibleCocaCola = new ArrayList<>();
	List<Actividad> actividadesMarketing = new ArrayList<>();
	List<Actividad> actividadesIT = new ArrayList<>();
	List<Miembro> miembrosITCocaCola = new ArrayList<>();
	List<Miembro> miembrosNoAceptadosITCocaCola = new ArrayList<>();

	TipoTransporte colectivo2 = new TipoTransporte("Colectivo"); // Bien
	TipoTransporte auto2 = new TipoTransporte("Auto"); // Bien

	Parada paradaLomas1 = new Parada(lomasDeZamora, 150.9000f, 210.1215f); // Bien
       // paradas145.add(paradaLomas); // Bien
	Parada paradaCaballito = new Parada(caballito, 181.4213f, 150.9000f); // Bien
       // paradas145.add(paradaCaballito); // Bien

	TransportePublico colectivo145 = new TransportePublico(colectivo, paradas145, "145", "Colectivo 145"); // Bien

       // paradaLomas.setTransportePublico(colectivo145);
       // paradaCaballito.setTransportePublico(colectivo145);

	Tramo tramoYayi = new Tramo(lomasDeZamora, caballito);
	Tramo tramoFran = new Tramo(caballito,lomasDeZamora); // Bien
        //tramosBondiYayiFran.add(tramoYayi); // Bien

	TransporteParticular ferrari1 = new TransporteParticular(auto, "NAFTA", "Peugeot 208");// *************************************

	Trayecto trayectoColectivo = new Trayecto(lomasDeZamora, caballito, colectivo145, tramosBondiYayiFran, 1); // **************
	// le
	// cambie
	// el
	// transporte
       //trayectosYayiFran.add(trayectoColectivo); // Bien

       // tramoYayi.setTrayecto(trayectoColectivo); // Bien

	Miembro franco = new Miembro("Franco", "Scolari", TipoDni.DNI, 42025156L, trayectosYayiFran); // Bien
	Miembro yayi = new Miembro("Yasmin", "Elias", TipoDni.DNI, 42148753L, trayectosYayiFran); // Bien

	TipoOrganizacion empresa = new TipoOrganizacion("Empresa SA");

	Organizacion cocaCola = new Organizacion("Coca-Cola", lomasDeZamora, sectoresCocaCola,
			Clasificacion.SECTOR_PRIMARIO, medibleCocaCola, empresa);

	Sector marketingCocaCola = new Sector("Marketing Coca Cola", cocaCola,actividadesMarketing,miembrosMarketingCocaCola,miembrosNoAceptadosMarketingCocaCola); // Bien
	Sector itCocaCola = new Sector("it Coca Cola", cocaCola,actividadesIT,miembrosITCocaCola,miembrosNoAceptadosITCocaCola); // Bien

	MiembroPorSector francoMarketingCocaCola = new MiembroPorSector(franco, marketingCocaCola, true);

       /* miembrosMarketingCocaCola.add(franco); // Bien
        marketingCocaCola.setMiembrosAceptados(miembrosMarketingCocaCola);// Bien
        sectoresCocaCola.add(marketingCocaCola);// Bien
        cocaCola.setSectores(sectoresCocaCola);// Bien*/

	Actividad mineria = new Actividad("Mineria", Alcance.EMISION_DIRECTA, marketingCocaCola);
	Actividad nuclear = new Actividad("Nuclear", Alcance.EMISION_DIRECTA, marketingCocaCola);

       /* actividadesMarketing.add(mineria);
        actividadesMarketing.add(nuclear);

        marketingCocaCola.setActividades(actividadesMarketing);

        sectoresCocaCola.add(marketingCocaCola);// Bien
        cocaCola.setSectores(sectoresCocaCola);// Bien*/

	PeriodoDeImputacion periodoImputacionMineria = new PeriodoDeImputacion(Mes.ENERO, 2022);
	PeriodoDeImputacion periodoImputacionNuclear = new PeriodoDeImputacion(Mes.ENERO, 2022);

	Unidad unidadM3 = new Unidad("M3");
	Unidad unidadGE = new Unidad("GECO2EQ", unidadM3);

	FactorEmision factorMineria = new FactorEmision(1, unidadGE);
	FactorEmision factorNuclear = new FactorEmision(2, unidadGE);

	TipoConsumo tipoConsumo1 = new TipoConsumo(unidadM3, factorMineria, "Distancia media recorrida");
	TipoConsumo tipoConsumoMal1 = new TipoConsumo(unidadM3, factorNuclear, "Gasoil");

	Consumo consumoMineria = new Consumo(tipoConsumo, 10f, Periodicidad.MENSUAL);
	Consumo consumoNuclear = new Consumo(tipoConsumoMal, 30f, Periodicidad.MENSUAL);

	Medicion medicion1 = new Medicion(mineria, consumoMineria, periodoImputacionMineria, cocaCola);
	Medicion medicion2 = new Medicion(nuclear, consumoNuclear, periodoImputacionNuclear, cocaCola);

      /*  medibleCocaCola.add(medicion1);
        medibleCocaCola.add(medicion2);
        cocaCola.setMedibles(medibleCocaCola);*/
	@Test
	void darDeAltaOrganizacionYAsignarSector() {
		cocaCola.addSector(marketingCocaCola);
		assertEquals(cocaCola.getSectores().get(0), marketingCocaCola);
	}

	@Test
	void vincularMiembroConOrg() {
		franco.vincularASector(marketingCocaCola);
		yayi.vincularASector(marketingCocaCola);

		assertEquals(marketingCocaCola.getMiembrosNoAceptados().size(), 2);
		assertEquals(marketingCocaCola.getMiembros().size(), 0);

	}

	@Test
	void aceptarMiembroEnSector() {
		franco.vincularASector(marketingCocaCola);
		yayi.vincularASector(marketingCocaCola);

		marketingCocaCola.aceptarMiembro(franco);

		assertTrue(marketingCocaCola.getMiembros().contains(franco));
		assertEquals(marketingCocaCola.getMiembros().size(), 1);
		assertEquals(marketingCocaCola.getMiembrosNoAceptados().size(), 1);
	}

	@Test
	void miembroNoPuedeUnirseADosSectoresDeMismaOrganizacionException() {
		sectoresCocaCola.add(marketingCocaCola);
		sectoresCocaCola.add(itCocaCola);


		cocaCola.setSectores(sectoresCocaCola);

		franco.vincularASector(marketingCocaCola);
		franco.vincularASector(itCocaCola);
		marketingCocaCola.aceptarMiembro(franco);

		MiembroNoPuedeUnirseAOrganizacionException thrown = assertThrows(
				MiembroNoPuedeUnirseAOrganizacionException.class,
				() -> itCocaCola.aceptarMiembro(franco),
				"El miembro ya pertenece a un sector de la organizacion");

		assertTrue(thrown.getMessage().contains("El miembro ya pertenece a un sector de la organizacion"));
	}
/*NO ENTIENDO ESTE TEST
	@Test
	void cargaMedicion() throws Exception {
		cocaCola.cargarParametrosDeArchivo("assets/parametros_sistema_example.csv");
		cocaCola.cargarMedicion("assets/medicion_example.csv"); // tener csv de prueba

		Medicion temp = (Medicion) cocaCola.getMedibles().get(0);

		assertEquals(temp.getActividad().getNombre(), "Combustion Fija");
		assertEquals(temp.getConsumo().getTipoConsumo().getNombre().toLowerCase(), "Gas Natural".toLowerCase());
	}

	@Test
	void cargaMedicionConActividadInexistente() {
		ActividadNoExisteException thrown = assertThrows(
				ActividadNoExisteException.class,
				() -> cocaCola.cargarMedicion("assets/medicion_actividad_inexistente.csv"),
				"La carga de medicion va a fallar porque el archivo contiene una actividad que no existe en el repo :D");

		assertTrue(thrown.getMessage().contains("No se encontro la actividad especificada"));
	}

	@Test
	void cargaMedicionConTipoConsumoInexistente() {
		TipoConsumoNoExisteException thrown = assertThrows(
				TipoConsumoNoExisteException.class,
				() -> cocaCola.cargarMedicion("assets/medicion_tipo_consumo_inexistente.csv"),
				"La carga de medicion va a fallar porque el archivo contiene un tipo de consumo que no existe en el repo :D");

		assertTrue(thrown.getMessage().contains("No se encontro el tipo de consumo especificado"));
	}

	@Test
	void cargarParametrosSistemaOrg() throws Exception {
		cocaCola.cargarParametrosDeArchivo("assets/parametros_sistema_example.csv");

		assertDoesNotThrow(() -> {
			repoTipoConsumo.getTipoConsumo("Gas Natural");
			repoTipoConsumo.getTipoConsumo("Diesel/Gasoil");
		});
	}

	@Test
	void cargaBienPeriodicidadMedicion() throws Exception {
		ParserCSV parserParams = new ParserCSV("assets/parametros_sistema_example.csv");
		Map<String, Float> params = parserParams.leerParametrosSistema();

		cocaCola.cargarParametros(params);
		cocaCola.cargarMedicion("assets/medicion_example.csv");

		assertEquals(
				consumo.getPeriodicidad().toString(),
				"ANUAL");
	}

	@Test
	void calculoHUConDosMediciones() throws Exception {
		cocaCola.cargarParametrosDeArchivo("assets/parametros_sistema_example.csv");
		cocaCola.cargarMedicion("assets/medicion_hu_test.csv"); // tener csv de prueba
		List<Medible> medibles = cocaCola.getMedibles();
		Float huellaCarbono = cocaCola.obtenerHU(medibles);
		assertEquals(huellaCarbono, 43.8054f);
	}
*/
	@Test
	void trayectoCompartidoPorMiembros() {
		marketingCocaCola.aceptarMiembro(franco);
		marketingCocaCola.aceptarMiembro(yayi);
		franco.addTrayecto(trayectoColectivo);
		yayi.addTrayecto(trayectoColectivo);
		assertTrue(franco.getTrayectos().contains(trayectoColectivo));
		assertTrue(yayi.getTrayectos().contains(trayectoColectivo));
	}

	@Test
	void calculoDeDistanciaDeTrayecto() {
		tramosBondiYayiFran.add(tramoYayi); // lomasdeZamora-canning
		//tramosBondiYayiFran.add(tramoFran); // canning-palermo

		paradas145.add(paradaLomas);
		paradas145.add(paradaCaballito);
		float distanciaTrayecto = trayectoColectivo.calcularDistancia();
		assertEquals(distanciaTrayecto, 141.4213f);
	}

	@Test
	void calculoDeDistanciaDeTrayecto1() {
		// Trayecto de lomasDeZamora a lanus

		tramos.add(tramo1); // lomasdeZamora-canning
		tramos.add(tramo2); // canning-palermo
		tramos.add(tramo3); // palermo-obelisco
		tramos.add(tramo4); // obelisco-lanus

		paradas.add(paradaLomas);
		paradas.add(paradaCanning);
		paradas.add(paradaPalermo);
		paradas.add(paradaObelisco);
		paradas.add(paradaLanus);

		float distanciaTrayecto = trayecto1.calcularDistancia();
		assertEquals(distanciaTrayecto, 1172.82850f);
	}

	@Test

	void distanciaLugarAlMismoLugar() {
		tramos.add(tramo6);
		paradas.add(paradaPrueba);
		float distanciaTrayecto1 = trayecto1.calcularDistancia();
		assertEquals(distanciaTrayecto1, 0f);
	}

	@Test
	void convierteBienLaUnidadDeFEdeTNaKG() {
		FactorEmision factorConvertido = otroTipoConsumo.convertirUnidad();
		String unidadFEConvertida = factorConvertido.getUnidad().getNombre();
		assertNotEquals(unidadTN.getNombre(), unidadFEConvertida);

	}

	@Test
	void convierteBienElValorDeFEdeTNaKG() {
		otroTipoConsumo.convertirUnidad();
		double valorFEConvertido = otroTipoConsumo.getFactorEmision().getValor();
		assertEquals(10000, valorFEConvertido);
	}
/*
	@Test
	void noCoincidenLasUnidades() {
		UnidadIncorrectaException thrown = assertThrows(
				UnidadIncorrectaException.class,
				() -> tipoConsumoMal.convertirUnidad(),
				"La conversion va a fallar porque no coincide la unidad del tipo consumo con la unidad dividendo del FE");

		assertTrue(thrown.getMessage().contains("No coinciden las unidades, revisar"));
	}

	@Test
	void testParserFactory() throws NoImplementadoException, IOException {
		String nomArchivoCSV = "assets/medicion_example.csv";
		String nomArchivoJSON = "assets/medicion_example.json";

		ParserTransporte parserTransporteJSON = (ParserTransporte) ParserFactory.crearParserTransporte(nomArchivoJSON);

		ParserMedicion parserMedicionCSV = (ParserMedicion) ParserFactory.crearParserMedicion(nomArchivoCSV);
		// ParserTransporte parserTransporteJSON = (ParserTransporte) ParserFactory.crea

		assertThrows(
				NoImplementadoException.class,
				() -> ParserFactory.crearParserMedicion(nomArchivoJSON),
				"El test en si fallara porque el parser de json no esta listo para parsear una medicion");

		assertEquals(parserMedicionCSV.getClass(), ParserCSV.class);
		assertTrue(parserMedicionCSV instanceof ParserMedicion);

		assertEquals(parserTransporteJSON.getClass(), ParserJSON.class);
		assertTrue(parserTransporteJSON instanceof ParserTransporte);
	}

	@Test
	void testParser() throws Exception {

		String archivoParadas = "assets/transporte_paradas.json";
		String archivoTrayectos = "assets/trayectos_todos_medios_de_transporte.csv";
		String archivoOrganizaciones = "assets/organizaciones.json";

		ParserTransporte parserTransporte = (ParserTransporte) ParserFactory.crearParserTransporte(archivoParadas);
		List<MedioDeTransporte> transportePublicos = parserTransporte.leerTransportes();

		ParserDatosOrganizacion parserOrg = (ParserDatosOrganizacion) ParserFactory
				.crearParserDatosOrganizacion(archivoOrganizaciones);
		List<Organizacion> organizaciones = parserOrg.leerOrganizaciones();

		ParserTrayecto parserTrayectos = (ParserTrayecto) ParserFactory.crearParserTrayecto(archivoTrayectos);
		List<Trayecto> trayectos = parserTrayectos.leerTrayectos();

		for (Organizacion org : organizaciones) {
			System.out.println(org.getRazon_social());

		}

		for (Trayecto trayecto : trayectos) {
			System.out.println(trayecto.getCantidadParticipantes());

		}

		// for(MedioDeTransporte transporte : transportePublicos){
		// System.out.println(transporte.getParadas());
		// //transporte.getParadas().forEach(parada ->
		// System.out.println(Float.toString(parada.getDistanciaParadaSiguiente())));

	}

	@Test
	void testFiltrosTrayectos() throws Exception {
		String archivoParadas = "assets/transporte_paradas.json";
		String archivoTrayectos = "assets/trayectos_todos_medios_de_transporte.csv";
		String archivoOrganizaciones = "assets/organizaciones.json";

		ParserTransporte parserTransporte = (ParserTransporte) ParserFactory.crearParserTransporte(archivoParadas);
		List<MedioDeTransporte> transportePublicos = parserTransporte.leerTransportes();

		ParserDatosOrganizacion parserOrg = (ParserDatosOrganizacion) ParserFactory
				.crearParserDatosOrganizacion(archivoOrganizaciones);
		List<Organizacion> organizaciones = parserOrg.leerOrganizaciones();

		ParserTrayecto parserTrayectos = (ParserTrayecto) ParserFactory.crearParserTrayecto(archivoTrayectos);
		List<Trayecto> trayectos = parserTrayectos.leerTrayectos();

		Organizacion org = organizaciones.get(0);
		Miembro miembro = org.getSectores().get(0).getMiembros().get(0);
		System.out.println(miembro.getTrayectosOrganizacion(org));

	}

	@Test
	void seLeAgregaTrayectoAMiembroYAumentaSuPropioHC() {
		// COMPLETAR
	}

	@Test
	void seLeAgregaTrayectoAMiembroYAumentaElHCDeSuOrganizacion() {
		// COMPLETAR
	}

	@Test
	void cambiaUnParametroDeUnaMedicionYCambiaElHCResultante() {

		EntityManagerHelper.beginTransaction();
		EntityManagerHelper.getEntityManager().persist(ferrari);
		EntityManagerHelper.commit();

	}

	@Test
	void elAgenteSectorialCalculaElHCdeUnSectorTerritorial() {
		// COMPLETAR
	}
*/


}
