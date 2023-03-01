package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.Agente;

public class AgenteRepositorio extends RepositorioBase<Agente> {
    private static AgenteRepositorio instancia = new AgenteRepositorio();

    public AgenteRepositorio() {
        super(Agente.class);
    }

    public static AgenteRepositorio getInstancia() {
        return instancia;
    }

}
