package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import java.util.HashMap;
import java.util.Map;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;;

public class ActividadRepositorio extends RepositorioBase<Actividad> {
    private static ActividadRepositorio instancia = new ActividadRepositorio();

    public ActividadRepositorio() {
        super(Actividad.class);
    }

    public static ActividadRepositorio getInstancia() {
        return instancia;
    }

    public Actividad getActividad(String nombre) {
        Map<String, String> values = new HashMap<>();
        values.put("nombre", "'" + nombre + "'");
        return instancia.getManyByValuesCustom(values).get(0);
    }


    // @Override
    // protected Actividad handleSaveDuplicated(Actividad obj) {
    //     return getByNombre(obj.getNombre());
    // }

    @Override
    protected Actividad handleSaveDuplicated(Actividad obj) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("nombre", "'" + obj.getNombre() + "'");
        params.put("sector_id", String.valueOf(obj.getSector().getId()));
        return getByValuesCustom(params);
    }

}
