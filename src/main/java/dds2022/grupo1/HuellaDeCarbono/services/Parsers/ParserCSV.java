package dds2022.grupo1.HuellaDeCarbono.services.Parsers;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserMedicion;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserParametrosSistema;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserTrayecto;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.MiembroRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Mes;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoImplementadoException;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.MedicionFactory;
import dds2022.grupo1.HuellaDeCarbono.services.Factories.MedioDeTransporteFactory;

import java.io.*;

import java.util.*;



public class ParserCSV extends ParserAbs implements ParserMedicion, ParserParametrosSistema, ParserTrayecto {
    private static final String separadorPrimario = ",";
    private static final String separadorSecundario = ";";
    private static final String separadorTerciario = ":";
    private static final String separadorCuaternario = "-";
    
    // private List<String> lineas = new ArrayList<>();
    
    public ParserCSV(String nombreArchivo) throws IOException{
        super(nombreArchivo);
        super.leerArchivo();
        super.leerLineas();
    }

    @Override
    public List<Medible> leerMediciones() throws NoExisteException {
        List<Medible> mediciones = new ArrayList<>();
        for(String linea : lineas){
            List<String> tempArr = Arrays.asList(linea.split(separadorPrimario));
            mediciones.add(MedicionFactory.getMedicion(tempArr));
        }
        return mediciones;
    }

    @Override
    public Map<String, Float> leerParametrosSistema() {
        Map<String, Float> result = new HashMap<>();
       
        for (String linea : lineas) {
            List<String> tempArr = Arrays.asList(linea.split(separadorPrimario));
            String nombreYTipo = tempArr.get(0) + "," + tempArr.get(1);
            Float factorEmision = Float.parseFloat(tempArr.get(2));
            result.put(nombreYTipo, factorEmision);
        }
       
        return result;
    }

    @Override
    public List<Trayecto> leerTrayectos() throws NoExisteException, NoImplementadoException{
        List<Trayecto> result = new ArrayList<>();
        
            for (String linea : lineas) {
                List<String> fila = Arrays.asList(linea.split(separadorPrimario));
                String [] salidaStr = fila.get(0).split(separadorSecundario);
                String [] llegadaStr = fila.get(1).split(separadorSecundario);
                
                Ubicacion salida = new Ubicacion(Float.parseFloat(salidaStr[0]), Float.parseFloat(salidaStr[1]));
                Ubicacion destino = new Ubicacion(Float.parseFloat(llegadaStr[0]), Float.parseFloat(llegadaStr[1]));

                MedioDeTransporte medioDeTransporte = MedioDeTransporteFactory.crearPorTipoVehiculo(Arrays.asList(fila.get(2).split(separadorSecundario)));

                List<String> tramosStr = Arrays.asList(fila.get(3).split(separadorSecundario));
                List<Tramo> tramos = new ArrayList<>();
                for(String tramoStr : tramosStr){
                    Ubicacion anterior;
                    Ubicacion actual;
                    Ubicacion proxima = null;
                    List<String> ubicacionesTramoStr = Arrays.asList(tramoStr.split(separadorTerciario));
                    
                    List<String> LatLngAnterior = Arrays.asList(ubicacionesTramoStr.get(0).split(separadorCuaternario));
                    List<String> LatLngActual = Arrays.asList(ubicacionesTramoStr.get(1).split(separadorCuaternario));
                    List<String> LatLngProxima;
                    if (ubicacionesTramoStr.size() > 2) {
                        LatLngProxima = Arrays.asList(ubicacionesTramoStr.get(2).split(separadorCuaternario));
                        proxima = new Ubicacion(Float.parseFloat(LatLngProxima.get(0)), Float.parseFloat(LatLngProxima.get(1)));
                    }

                    anterior = new Ubicacion(Float.parseFloat(LatLngAnterior.get(0)), Float.parseFloat(LatLngAnterior.get(1)));
                    actual = new Ubicacion(Float.parseFloat(LatLngActual.get(0)), Float.parseFloat(LatLngActual.get(1)));
                    tramos.add(new Tramo(actual, anterior, proxima));
                    // tramo; tramo2
                    // tramo => lat-lng: lat-lng: lat-lng; ..... ,
                }

                List<String> participantesStr = Arrays.asList(fila.get(4).split(separadorSecundario));
                List<Miembro> participantes = new ArrayList<>();
//                System.out.println(participantesStr.get(0));
                participantesStr.forEach(
                    dni -> participantes.add(
                        MiembroRepositorio.getInstancia().getMiembro(
                            Long.parseLong(dni)
                            )
                        )
                    );
                int cantidadParticipantes = participantes.size();
                //5
                List<String> periodo = Arrays.asList(fila.get(5).split(separadorSecundario));
                PeriodoDeImputacion periodoImputacion;
                if (periodo.size() > 1) {
                    int nroMes = Integer.parseInt(periodo.get(0)) - 1;
                    int nroAnio = Integer.parseInt(periodo.get(1));
                    periodoImputacion = new PeriodoDeImputacion(Mes.values()[nroMes], nroAnio);
                } else {
                    int nroAnio = Integer.parseInt(periodo.get(0));
                    periodoImputacion = new PeriodoDeImputacion(nroAnio);

                }
                Trayecto trayecto = new Trayecto(salida, destino, medioDeTransporte, tramos, cantidadParticipantes, periodoImputacion);
                result.add(trayecto);
                participantes.forEach(miembro -> miembro.addTrayecto(trayecto));

                /*
                 *  -salida:Ubicacion
                    -destino:Ubicacion
                    -medioDeTransporte:MedioDeTransporte
                    -tramos:List<Tramo>
                    -cantidadParticipantes: int
                 */

                //latSalida;lonSalida, latLlegada;lonLlegada, bicicleta, latAnte-lonAnte:latactual-lonactual:latsig-lonsig;latAnte-lonAnte:latactual-lonactual:latsig-lonsig,  123132;123;123  
            }
    
        return result;
    }

    
}



