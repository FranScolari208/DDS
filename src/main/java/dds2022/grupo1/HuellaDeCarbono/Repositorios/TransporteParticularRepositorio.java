package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteParticular;

public class TransporteParticularRepositorio extends RepositorioBase<TransporteParticular> {
    private static TransporteParticularRepositorio instancia = new TransporteParticularRepositorio();

    public TransporteParticularRepositorio() {
        super(TransporteParticular.class);
    }

    public static TransporteParticularRepositorio getInstancia() {
        return instancia;
    }

}
