package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import java.util.HashMap;
import java.util.Map;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;

public class TipoConsumoRepositorio extends RepositorioBase<TipoConsumo> {
    private static TipoConsumoRepositorio instancia = new TipoConsumoRepositorio();

    public TipoConsumoRepositorio() {
        super(TipoConsumo.class);
    }

    public static TipoConsumoRepositorio getInstancia() {
        return instancia;
    }

    public TipoConsumo getTipoConsumo(String nombre) {
        Map<String, String> values = new HashMap<>();
        values.put("nombre", nombre);
        return instancia.getByStringValues(values, false);
    }
    
    @Override
    protected TipoConsumo handleSaveDuplicated(TipoConsumo obj) {
        return getByNombre(obj.getNombre());
    }

}
