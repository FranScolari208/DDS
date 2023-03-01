package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteEcologico;

public class TransporteEcologicoRepositorio extends RepositorioBase<TransporteEcologico> {
    private static TransporteEcologicoRepositorio instancia = new TransporteEcologicoRepositorio();

    public TransporteEcologicoRepositorio() {
        super(TransporteEcologico.class);
    }

    public static TransporteEcologicoRepositorio getInstancia() {
        return instancia;
    }

}
