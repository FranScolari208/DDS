package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TipoTransporte;

public class TipoTransporteRepositorio extends RepositorioBase<TipoTransporte> {
    private static TipoTransporteRepositorio instancia = new TipoTransporteRepositorio();

    public TipoTransporteRepositorio() {
        super(TipoTransporte.class);
    }

    public static TipoTransporteRepositorio getInstancia() {
        return instancia;
    }

    public TipoTransporte getByName(String nombre) {
        return getBy("nombre", nombre);
    }

    @Override
    protected TipoTransporte handleSaveDuplicated(TipoTransporte obj) {
        // getByValues <String String>, OR=False, numerico=False
        // Map<String, String> hm = new HashMap<String, String>();
        // hm.put("name");
        return getByNombre(obj.toString());
    }

}
