package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.FactorEmision;

public class FactorEmisionRepositorio extends RepositorioBase<FactorEmision> {
    private static FactorEmisionRepositorio instancia = new FactorEmisionRepositorio();

    public FactorEmisionRepositorio() {
        super(FactorEmision.class);
    }

    public static FactorEmisionRepositorio getInstancia() {
        return instancia;
    }

}
