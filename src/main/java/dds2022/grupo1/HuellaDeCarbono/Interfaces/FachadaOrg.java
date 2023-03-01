package dds2022.grupo1.HuellaDeCarbono.Interfaces;

import java.util.Collection;
import java.util.Map;

public interface FachadaOrg {

    void cargarParametros(Map<String, Float> parametrosSistema);
    //string -> nombre_actividad, float-> FE actividad

    Float obtenerHU(Collection<Medible> mediciones);
}