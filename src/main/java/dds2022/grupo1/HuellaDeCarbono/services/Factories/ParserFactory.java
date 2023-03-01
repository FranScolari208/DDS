package dds2022.grupo1.HuellaDeCarbono.services.Factories;
import java.io.FileNotFoundException;
import java.io.IOException;

import dds2022.grupo1.HuellaDeCarbono.exceptions.NoImplementadoException;
import dds2022.grupo1.HuellaDeCarbono.services.Parsers.ParserCSV;
import dds2022.grupo1.HuellaDeCarbono.services.Parsers.ParserJSON;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.Parser;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserDatosOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserMedicion;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserParametrosSistema;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserTransporte;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserTrayecto;
public class ParserFactory {

    public static Parser crearPorExtension(String filename) throws NoImplementadoException, IOException {

        if (filename == null) {throw new FileNotFoundException();}
        // List<String> parsedName =  Arrays.asList(filename.split("."));
        // String fileExtension = parsedName.get(1).toLowerCase();
        String fileExtension = filename.split("\\.")[1];
        switch(fileExtension){
            case "csv":
                return new ParserCSV(filename);
            case "json":
                return new ParserJSON(filename);
            default:
                throw new NoImplementadoException("No se implemento un parser para un tipo diferente a .csv o .json");
        }
    }

    public static ParserMedicion crearParserMedicion(String archivo) throws NoImplementadoException, IOException {        
            Parser temp =  crearPorExtension(archivo);
            if (temp instanceof ParserMedicion){
                return (ParserMedicion) temp;
            }
            throw new NoImplementadoException("No se implemento parser de mediciones para ese tipo de archivo aun");
    }

    public static ParserTrayecto crearParserTrayecto(String archivo) throws NoImplementadoException, IOException {        
        Parser temp =  crearPorExtension(archivo);
        if (temp instanceof ParserTrayecto){
            return (ParserTrayecto) temp;
        }
        throw new NoImplementadoException("No se implemento parser de trayectos para ese tipo de archivo aun");
    }

    public static ParserTransporte crearParserTransporte(String archivo) throws NoImplementadoException, IOException {        
        Parser temp =  crearPorExtension(archivo);
        if (temp instanceof ParserTransporte){
            return (ParserTransporte) temp;
        }
        throw new NoImplementadoException("No se implemento parser de transportes para ese tipo de archivo aun");
    }

    public static ParserDatosOrganizacion crearParserDatosOrganizacion(String archivo) throws NoImplementadoException, IOException {        
        Parser temp =  crearPorExtension(archivo);
        if (temp instanceof ParserDatosOrganizacion){
            return (ParserDatosOrganizacion) temp;
        }
        throw new NoImplementadoException("No se implemento parser de datos de orgs para ese tipo de archivo aun");
    }

    public static ParserParametrosSistema crearParserParametrosSistema(String archivo) throws NoImplementadoException, IOException {        
        Parser temp =  crearPorExtension(archivo);
        if (temp instanceof ParserParametrosSistema){
            return (ParserParametrosSistema) temp;
        }
        throw new NoImplementadoException("No se implemento parser de params de sistema para ese tipo de archivo aun");
    }

}
