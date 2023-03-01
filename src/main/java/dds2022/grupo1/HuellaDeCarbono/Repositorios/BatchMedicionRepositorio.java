package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.BatchMedicion;

public class BatchMedicionRepositorio extends RepositorioBase<BatchMedicion> {
    private static BatchMedicionRepositorio instancia = new BatchMedicionRepositorio();

    public BatchMedicionRepositorio() {
        super(BatchMedicion.class);
    }

    public static BatchMedicionRepositorio getInstancia() {
        return instancia;
    }

}
