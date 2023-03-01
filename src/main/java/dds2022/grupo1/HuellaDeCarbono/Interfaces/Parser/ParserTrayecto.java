package dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser;
import java.util.List;

import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;

public interface ParserTrayecto extends Parser {
    public List<Trayecto> leerTrayectos() throws Exception;
    
}
