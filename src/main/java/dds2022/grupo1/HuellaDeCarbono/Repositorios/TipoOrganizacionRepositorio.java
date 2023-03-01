package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.TipoOrganizacion;

import java.util.List;

public class TipoOrganizacionRepositorio extends RepositorioBase<TipoOrganizacion> {
    private static TipoOrganizacionRepositorio instancia = new TipoOrganizacionRepositorio();
    TipoConsumoRepositorio tipoConsumoRepositorio = TipoConsumoRepositorio.getInstancia();
    List<TipoConsumo> tipoconsumos = tipoConsumoRepositorio.list();

    public TipoOrganizacionRepositorio() {
        super(TipoOrganizacion.class);
    }

    public static TipoOrganizacionRepositorio getInstancia() {
        return instancia;
    }

    @Override
    protected TipoOrganizacion handleSaveDuplicated(TipoOrganizacion obj) {
        return getBy("descripcion", obj.getDescripcion());
    }

}
