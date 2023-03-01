package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;

public class TramoRepositorio extends RepositorioBase<Tramo> {
    private static TramoRepositorio instancia = new TramoRepositorio();

    public TramoRepositorio() {
        super(Tramo.class);
    }

    public static TramoRepositorio getInstancia() {
        return instancia;
    }

}
