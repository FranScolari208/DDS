package dds2022.grupo1.HuellaDeCarbono.entidades.Sector;


public class Distancia {
    private double valor;
    private String unidad;

    public Distancia(float valor, String unidad) {
        this.valor = valor;
        this.unidad = unidad;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
