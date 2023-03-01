package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.TipoOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.Agente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.Municipio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.SectorTerritorial;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Clasificacion;

import java.util.List;

public class MailMain {

    public static void main(String[]args ) throws Exception{
        TipoOrganizacion tipo= new TipoOrganizacion("Gubernamental");
        Ubicacion ubicacion = new Ubicacion("berlin",12.9f,23.9f);
        Organizacion org = new Organizacion("sa", tipo,ubicacion, Clasificacion.GUBERNAMENTAL);
        SectorTerritorial sector = new Municipio("nombre",(List<Organizacion>) org);
        Agente agente = new Agente("ceci", "ceciliaarocca@gmail.com", "1132456798", sector);
        Mailer.enviar("Reporte",agente);
    }
}
