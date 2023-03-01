package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.exceptions.UnidadIncorrectaException;

import javax.persistence.*;

import org.json.JSONObject;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre" }) })
@NamedQuery(name = "getAllTipoConsumo", query = "select a from TipoConsumo a")
public class TipoConsumo extends Persistente {
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "unidad_id", referencedColumnName = "id")
    private Unidad unidad;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "factorEmision_id", referencedColumnName = "id")
    private FactorEmision factorEmision;
    @Column
    private String nombre;

    @Override
    public String toString() {
        return new JSONObject()
        .put("id", id)
        .put("nombre", nombre)
        .put("factor_emision", new JSONObject(factorEmision.toString()))
        .put("unidad", unidad.toString())
        .toString();
    }

    public TipoConsumo(Unidad unidad, FactorEmision factorEmision, String nombre) {
        this.unidad = unidad;
        this.factorEmision = factorEmision;
        this.nombre = nombre;
    }

    public TipoConsumo() {
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public FactorEmision getFactorEmision() {
        return factorEmision;
    }

    public String getNombre() {
        return nombre;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public void setFactorEmision(FactorEmision factorEmision) {
        this.factorEmision = factorEmision;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void equivalenteConsumo(String nombreUnidad) {
        Double valorNuevo;
        if (nombreUnidad.equals("GEC02EQ")) {

            valorNuevo = factorEmision.getValor() / 1000;
        } else {
            valorNuevo = factorEmision.getValor() * 1000;
        }
        factorEmision.setValor(valorNuevo);
    }

    public FactorEmision convertirUnidad() {

        String unidadFEDividendo = factorEmision.getUnidad().getUnidadDividendo().getNombre();
        String unidadTipoConsumo = unidad.getNombre();
        String unidadFENombre = factorEmision.getUnidad().getNombre();

        if (unidadFENombre.equals("GEC02EQ") || unidadFENombre.equals("TNEC02EQ")) {
            this.equivalenteConsumo(unidadFENombre);
            unidadFENombre = "KGCO2EQ";
        }
        if (unidadTipoConsumo.equals(unidadFEDividendo)) {
            Unidad nuevaUnidad = new Unidad(unidadFENombre);
            FactorEmision resultadoUnidad = new FactorEmision(factorEmision.getValor(), nuevaUnidad);
            return resultadoUnidad;
        } else {
            throw new UnidadIncorrectaException("No coinciden las unidades, revisar");
        }
    }

    public Unidad convertidorUnidad() {

        this.convertirUnidad();
        return factorEmision.getUnidad();

    }

}
