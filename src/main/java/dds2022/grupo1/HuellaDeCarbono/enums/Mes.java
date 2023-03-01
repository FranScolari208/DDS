package dds2022.grupo1.HuellaDeCarbono.enums;

import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

public enum Mes {
    ENERO(1), FEBRERO(2), MARZO(3), ABRIL(4), MAYO(5), JUNIO(6), JULIO(7), AGOSTO(8), SEPTIEMBRE(9),
    OCTUBRE(10), NOVIEMBRE(11), DICIEMBRE(12);

    public int valor;

    private Mes(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static Mes getMesPorNumero(int numero) {
        for (Mes mes : Mes.values()) {
            if (mes.getValor() == numero) {
                return mes;
            }
        }
        throw new NoExisteException("El mes especificado no existe");
    }
}
