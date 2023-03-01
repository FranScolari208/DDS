package dds2022.grupo1.HuellaDeCarbono.CalculadorHU;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserMedicion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Alcance;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.ParserFactory;
import dds2022.grupo1.HuellaDeCarbono.services.Parsers.ParserCSV;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CalculadorHU {

    public static void main(String[] args) throws Exception {


        ArgumentParser parser = ArgumentParsers.newFor("Checksum").build()
                .defaultHelp(true)
                .description("Calculate checksum of given files.");
        parser.addArgument("-m", "--mediciones").required(true)
                .help("Archivo de mediciones");
        parser.addArgument("-p", "--params").required(true)
                .help("Archivo con parámetros de configuración");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }


        System.out.println("Archivo de mediciones: " + ns.get("mediciones"));
        System.out.println("Archivo de parametros: " + ns.get("params"));

        //asumimos ambos archivos como csv
        //instanciamos todas las actividades

        //vienen dentro de los params de sistema las actividades actuales???
        ParserCSV parserParams = new ParserCSV(ns.get("params"));
        Map<String, Float> params = parserParams.leerParametrosSistema();

        //cargamos en el repo los tipos de consumo con sus respectvos FE
        Organizacion org = new Organizacion();
        org.cargarParametros(params);
        org.setRazon_social("CocaCola");

        Actividad combustionFija = new Actividad("Combustion Fija", Alcance.EMISION_DIRECTA);

        ParserMedicion parserMediciones = ParserFactory.crearParserMedicion(ns.get("mediciones"));
        List<Medible> mediciones = new ArrayList<>();
        mediciones.addAll(parserMediciones.leerMediciones());

        Float HUOrg = org.obtenerHU(mediciones);
        String HCUnidad = org.obtenerUnidad(mediciones);

        System.out.println("Imprimir datos de las huellas");
        System.out.println(String.format("La huella de la organizacion %s es %f %s ",org.getRazon_social(),HUOrg, HCUnidad ) );
    }

}
