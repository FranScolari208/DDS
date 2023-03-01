package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.services.LogCalculos;

public class LogCalculosRepositorio extends RepositorioBase<LogCalculos> {
    private static LogCalculosRepositorio instancia = new LogCalculosRepositorio();

    public LogCalculosRepositorio() {
        super(LogCalculos.class);
    }

    public static LogCalculosRepositorio getInstancia() {
        return instancia;
    }

}
