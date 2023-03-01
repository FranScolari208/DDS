package dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser;

import java.util.List;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;

public interface ParserTransporte  extends Parser {
    public List<MedioDeTransporte> leerTransportes() throws Exception;
    
}
