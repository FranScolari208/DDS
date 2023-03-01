package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.Parada;

import java.util.List;

public class ParadaRepositorio extends RepositorioBase<Parada> {
    private static ParadaRepositorio instancia = new ParadaRepositorio();

    public ParadaRepositorio() {
        super(Parada.class);
    }

    public static ParadaRepositorio getInstancia() {
        return instancia;
    }

}
