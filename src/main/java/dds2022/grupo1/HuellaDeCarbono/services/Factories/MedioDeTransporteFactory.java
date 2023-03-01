package dds2022.grupo1.HuellaDeCarbono.services.Factories;

import java.util.List;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.MedioDeTransporteRepositorio;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

public class MedioDeTransporteFactory {
    public static MedioDeTransporte crearPorTipoVehiculo(List<String> datosTransporte) throws NoExisteException {
        /*
         * por lo que estoy viendo nos conviene tener clas de tipo de transporte y esto
         * sea lo que cambie entre transportes no publicos, en vez de que sean clases
         * solas
         * 
         * si es publico -> tipoTransporte
         * -> linea
         * 
         * si es otro -> nombre
         * 
         * si es particular -> tipoVehiculo
         * -> tipoCombustible
         * 
         * si es contratado -> tipoTransporte
         */

        // vemos si es publico o no publico (eso con buscar en el repo de publkicos
        // /creo/)
        // si es publico, ez
        // si no lo es, y tiene 2 args es particular
        // sino es uno sustentable o contratado, como diferenciarlos? ni idea
        // seguramente lo tengamos precargao y con una query al repo de los tipos sea
        // suficiente

        // TipoTransporte tipoVehiculo =
        // TipoTransporteRepositorio.getInstancia().getTipoTransporte((datosTransporte.get(0)));
        String tipoTrans = datosTransporte.get(0);
        MedioDeTransporte transporte;
        MedioDeTransporteRepositorio repoTransporte = MedioDeTransporteRepositorio.getInstancia();

        try {
            // System.out.println("crear por vehiculo: " + datosTransporte.get(0) + " " +
            // Integer.toString(datosTransporte.size()));
            // System.out.println(" -> "+ datosTransporte.get(1));
            String combustible_o_linea = datosTransporte.get(1);
            transporte = repoTransporte.getTransportePublico(combustible_o_linea);
        } catch (Exception e) {
            transporte = repoTransporte.getTransporteNoPublico(tipoTrans);
        }

        return transporte;
    }
}
