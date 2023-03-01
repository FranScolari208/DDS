package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;

import java.util.HashMap;
import java.util.Map;

public class UbicacionRepositorio extends RepositorioBase<Ubicacion> {
    private static UbicacionRepositorio instancia = new UbicacionRepositorio();

    public UbicacionRepositorio() {
        super(Ubicacion.class);
    }

    public static UbicacionRepositorio getInstancia() {
        return instancia;
    }

    @Override
    protected Ubicacion handleSaveDuplicated(Ubicacion obj) {
        Map<String, Number> params = new HashMap<String, Number>();
        params.put("latitud", obj.getLatitud());
        params.put("longitud", obj.getLongitud());
        return getByNumberValues(params, false);
    }
}
