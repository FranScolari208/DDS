package dds2022.grupo1.HuellaDeCarbono.CalculadorHU;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserDatosOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserTransporte;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserTrayecto;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.*;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.enums.Periodicidad;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.ParserFactory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dds2022.grupo1.HuellaDeCarbono.enums.Alcance.EMISION_INDIRECTA;

public class TrayectosHC {

    public static void main(String[] args) throws Exception {

        ArgumentParser parser = ArgumentParsers.newFor("Checksum").build()
                .defaultHelp(true)
                .description("Calculate checksum of given files.");
        parser.addArgument("-do", "--datos-organizacion").required(true)
                .help("Archivo de datos de organizacion");
        parser.addArgument("-dt", "--datos-transporte").required(true)
                .help("Archivo de datos de transporte");
        parser.addArgument("-dtray", "--datos-trayecto").required(true)
                .help("Archivo de datos de trayectos");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        // TRANSPORTE,ORG,TRAYECTOS

        String dir = System.getProperty("user.dir");
        System.out.println(dir);

        System.out.println("Archivo de org: " + ns.get("datos_organizacion"));
        System.out.println("Archivo de transporte: " + ns.get("datos_transporte"));
        System.out.println("Archivo de trayectos: " + ns.get("datos_trayecto"));

        // String nombre, Alcance alcance
        Actividad actividadTransporte = new Actividad("Traslado de Miembros de la Organizacion", EMISION_INDIRECTA);
        // Distancia media recorrida
        // TipoConsumo tipoConsumo, Float valor, Periodicidad periodicidad
        // Unidad unidad, FactorEmision factorEmision, String nombre

        TipoConsumo tipoConsumo = new TipoConsumo(new Unidad("KM"),
                new FactorEmision(2, new Unidad("KGCO2EQ", new Unidad("KM"))), "Distancia media recorrida");
        TipoConsumoRepositorio.getInstancia().save(tipoConsumo);
        Consumo consumo = new Consumo(tipoConsumo, 1.1f, Periodicidad.MENSUAL);

        ParserDatosOrganizacion parserOrg = ParserFactory.crearParserDatosOrganizacion(ns.get("datos_organizacion"));
        ParserTransporte parserTransporte = ParserFactory.crearParserTransporte(ns.get("datos_transporte"));
        ParserTrayecto parserTrayecto = ParserFactory.crearParserTrayecto(ns.get("datos_trayecto"));

        List<Organizacion> organizaciones = parserOrg.leerOrganizaciones();
        List<MedioDeTransporte> transportes = parserTransporte.leerTransportes();
        List<Trayecto> trayectos = parserTrayecto.leerTrayectos();
        System.out.println("cantidad de orgs leidas" + organizaciones.size());
        csv_crearArchivoMiembroOrgPeriodo(organizaciones);
        csv_crearArchivoSectorOrganizacionPeriodo(organizaciones);
        /*
         * Año y mes [AAAAMes],Razón Social Org, DNI Miembro, Impacto (% HC de trayectos
         * , con 2 decimales de precisión -
         * use punto “.” como separador decimal, los de una org/periodo tienen que sumar
         * 100)
         * 
         * Ejemplo:
         * 202201,Arcor,31604907,2
         * 202201,Arcor,44330580,10
         * 202201,Arcor,44430593,10
         * 202201,Arcor,43330999,78
         * 202202,Hospital Garrahan,20111689,30
         * 202202,Hospital Garrahan,43330999,20
         * 
         */
    }

    private static void csv_crearArchivoMiembroOrgPeriodo(List<Organizacion> orgs) throws IOException {
        List<List<String>> lineas = new ArrayList<>();
        for (Organizacion org : orgs) {
            for (Sector sector : org.getSectores()) {

                System.out.println("cant de miembros en " + " " + org.getRazon_social() + " " + sector.getMiembros().size());
                
                for (Miembro miembro : sector.getMiembros()) {
                    for (PeriodoDeImputacion periodo : miembro.getPeriodosDeImputacionDeTrayectos(org)) {
                        System.out.println(periodo.toString());
                        List<String> linea = new ArrayList<>();

                        Double HCMiembroOrgPeriodo;
                        Float result = 0f;
                        List<Trayecto> tray = miembro.getTrayectosOrganizacion(org);
                        for (Trayecto trash : tray) {
                            result = trash.calcularHCDivididoParticipantes();
                        }

                        // HCMiembroOrgPeriodo =
                        // miembro.getTrayectosOrganizacion(org).stream().mapToDouble(
                        // Trayecto::calcularHCDivididoParticipantes
                        // ).sum();
                        System.out.println("cant de trayectos para  " + miembro.getNombre() + "en "
                                + org.getRazon_social() + " " + miembro.getTrayectosOrganizacion(org).size());

                        // for(Trayecto tray : miembro.getTrayectosOrganizacion(org)){
                        // System.out.println(tray.calcularHCDivididoParticipantes());
                        // }
                        // System.out.println(HCMiembroOrgPeriodo);
                        linea.add(periodo.toString());
                        linea.add(org.getRazon_social());
                        linea.add(Long.toString(miembro.getDni()));
                        // linea.add(Double.toString(HCMiembroOrgPeriodo));
                        linea.add(Float.toString(result));
                        lineas.add(linea);
                    }
                }
            }
        }
        // open file etc etc
        CSVWriter writer = new CSVWriter("HC_por_periodo_organizacion_miembro.csv");
        writer.escribirLineas(lineas);
        // chequear si anda

    }

    private static void csv_crearArchivoSectorOrganizacionPeriodo(List<Organizacion> orgs) throws IOException {
        List<List<String>> lineas = new ArrayList<>();

        for (Organizacion org : orgs) {
            for (Sector sector : org.getSectores()) {
                // mergeo los periodos de imputacion de los miembros en una lista de periodos
                // iteramos la lista y hacemos el foreach de miembros aplicando el metodo de
                // getTrayectosOrgPeriodo con el periodo iterado
                // y lo sumamos entre todos los miembros
                // eso es el HC de ese periodo para ese sector
                List<PeriodoDeImputacion> periodos = new ArrayList<>();
                sector.getMiembros().stream()
                        .forEach(miembro -> periodos.addAll(miembro.getPeriodosDeImputacionDeTrayectos(org)));

                List<PeriodoDeImputacion> periodosNoRepetidos = new ArrayList<>();
                for (PeriodoDeImputacion periodo : periodos) {
                    if (!periodosNoRepetidos.stream().map(
                            PeriodoDeImputacion::toString).collect(Collectors.toList())
                            .contains(periodo.toString())) {
                        periodosNoRepetidos.add(periodo);
                    }
                }

                for (PeriodoDeImputacion periodo : periodosNoRepetidos) {
                    List<String> linea = new ArrayList<>();
                    List<Trayecto> trayectosSectorPeriodo = new ArrayList<>();
                    sector.getMiembros().stream().forEach(miembro -> trayectosSectorPeriodo
                            .addAll(miembro.getTrayectosPeriodoOrganizacion(periodo, org)));
                    int totalMiembrosSector = (int) Math.round(trayectosSectorPeriodo.stream()
                            .mapToDouble(Trayecto::calcularHCDivididoParticipantes).sum());
                    linea.add(periodo.toString());
                    linea.add(org.getRazon_social());
                    linea.add(sector.getNombre());
                    System.out.println("valor del total de los miembros del sector " + sector.getNombre()
                            + String.valueOf(totalMiembrosSector));
                    linea.add(String.valueOf(totalMiembrosSector));
                    lineas.add(linea);
                }

            }
        }
        CSVWriter writer = new CSVWriter("HC_por_periodo_organizacion_sector.csv");
        writer.escribirLineas(lineas);

    }

}

class CSVWriter {
    String archivo;

    public CSVWriter(String nombreArch) {
        archivo = nombreArch;
    }

    public void escribirLineas(List<List<String>> lineas) throws IOException {

        try {
            FileWriter writer = new FileWriter(archivo, false);
            parsearLineas(lineas).stream().forEach(linea -> {
                try {
                    writer.write(linea);
                    writer.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> parsearLineas(List<List<String>> lineas) {
        List<String> result = new ArrayList<>();
        for (List<String> linea : lineas) {
            result.add(String.join(",",
                    linea.stream().map(campo -> campo.replace(",", ".")).collect(Collectors.toList())));
        }
        return result;
    }
}