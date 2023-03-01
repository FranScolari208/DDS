package dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser;

import java.util.List;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;

public interface ParserDatosOrganizacion extends Parser {
    public List<Organizacion> leerOrganizaciones();
}
