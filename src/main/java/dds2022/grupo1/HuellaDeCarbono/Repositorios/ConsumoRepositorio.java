package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;

public class ConsumoRepositorio extends RepositorioBase<Consumo> {
    private static ConsumoRepositorio instancia = new ConsumoRepositorio();

    public ConsumoRepositorio() {
        super(Consumo.class);
    }

    public static ConsumoRepositorio getInstancia() {
        return instancia;
    }

}
