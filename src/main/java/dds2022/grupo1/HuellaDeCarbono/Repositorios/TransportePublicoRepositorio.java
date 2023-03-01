package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransportePublico;

public class TransportePublicoRepositorio extends RepositorioBase<TransportePublico> {
    private static TransportePublicoRepositorio instancia = new TransportePublicoRepositorio();

    public TransportePublicoRepositorio() {
        super(TransportePublico.class);
    }

    public static TransportePublicoRepositorio getInstancia() {
        return instancia;
    }

}
