package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("transporteParticular")
public class TransporteParticular extends TransporteNoPublico {
    @Column
    private String tipoCombustible;

    public TransporteParticular() {
    }

    public TransporteParticular(TipoTransporte tipoTransporte, String combustible) {
        super(tipoTransporte);
        this.tipoCombustible = combustible;
    }

    public TransporteParticular(TipoTransporte tipoTransporte, String combustible, String descripcion) {
        super(tipoTransporte, descripcion);
        this.tipoCombustible = combustible;
    }
}
