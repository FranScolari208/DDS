package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("transporteEcologico")
public class TransporteEcologico extends TransporteNoPublico {

    public TransporteEcologico(TipoTransporte transporte) {
        super(transporte);

    }

    public TransporteEcologico(TipoTransporte transporte, String descripcion) {
        super(transporte, descripcion);

    }

    public TransporteEcologico() {
        super();
    }

    @Override
    public Consumo calcularConsumo(float distanciaRecorrida) {
        Consumo result = super.calcularConsumo(distanciaRecorrida);
        result.setValor(0f);
        return result;
    }

}
