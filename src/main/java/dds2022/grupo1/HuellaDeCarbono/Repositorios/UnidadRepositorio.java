package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Unidad;

public class UnidadRepositorio extends RepositorioBase<Unidad> {
    private static UnidadRepositorio instancia = new UnidadRepositorio();

    public UnidadRepositorio() {
        super(Unidad.class);
    }

    public static UnidadRepositorio getInstancia() {
        return instancia;
    }

    @Override
    protected Unidad handleSaveDuplicated(Unidad obj) {
        return getByNombre(obj.getNombre());
    }

}
