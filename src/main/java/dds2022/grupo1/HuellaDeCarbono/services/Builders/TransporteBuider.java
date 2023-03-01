/*package dds2022.grupo1.HuellaDeCarbono.services.Builders;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoTransporteRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TipoTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteParticular;

public class TransporteBuider {

    public static TransporteParticular crearTransporteParticular(String nombreTransporte, String combustible) {
        TipoTransporte tipoTransporte = TipoTransporteRepositorio.getInstancia().getByName(nombreTransporte);
        return new TransporteParticular(tipoTransporte, combustible);
    }

}
*/