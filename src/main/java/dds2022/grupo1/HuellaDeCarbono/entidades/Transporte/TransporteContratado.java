package dds2022.grupo1.HuellaDeCarbono.entidades.Transporte;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("transporteContratado")
public class TransporteContratado extends TransporteNoPublico {
  public TransporteContratado(TipoTransporte tipoTransporte) {
    super(tipoTransporte);

  }

  public TransporteContratado(TipoTransporte tipoTransporte, String descripcion) {
    super(tipoTransporte, descripcion);

  }
  public TransporteContratado() {

  }
}
