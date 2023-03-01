package dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser;

import java.util.List;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;

public interface ParserMedicion extends Parser {
    public List<Medible> leerMediciones();
}
