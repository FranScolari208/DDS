
package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Medicion;

public class MedicionRepositorio extends RepositorioBase<Medicion> {
    private static MedicionRepositorio instancia = new MedicionRepositorio();

    public MedicionRepositorio() {
        super(Medicion.class);
    }

    public static MedicionRepositorio getInstancia() {
        return instancia;
    }

}
