package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import javax.persistence.NoResultException;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteNoPublico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransportePublico;

public class MedioDeTransporteRepositorio extends RepositorioBase<MedioDeTransporte> {
    private static MedioDeTransporteRepositorio instancia = new MedioDeTransporteRepositorio();

    private MedioDeTransporteRepositorio() {
        super(MedioDeTransporte.class);
    }

    public static MedioDeTransporteRepositorio getInstancia() {
        return instancia;
    }

    public TransportePublico getTransportePublico(String linea) {
        MedioDeTransporte rta = getBy("linea", linea);
        return (TransportePublico) rta;

    }

    public TransporteNoPublico getTransporteNoPublico(String nombre) {
        MedioDeTransporte rta = getBy("descripcion", nombre);
        return (TransporteNoPublico) rta;
    }

    public boolean existe(String nombre) {
        try {
            getTransportePublico(nombre);
            return true;
        } catch (NoResultException e) {
            try {
                getTransporteNoPublico(nombre);
                return true;
            } catch (NoResultException e2) {
                return false;
            }
        }
    }

}

// public class MedioDeTransporteRepositorio {
// List<TransporteNoPublico> transportesNoPublicos = new ArrayList<>();
// List<TransportePublico> transportesPublicos = new ArrayList<>();

// private static MedioDeTransporteRepositorio instancia = new
// MedioDeTransporteRepositorio();

// private MedioDeTransporteRepositorio() {
// // private to prevent anyone else from instantiating
// }

// public static MedioDeTransporteRepositorio getInstancia() {
// return instancia;
// }

// public TransportePublico getTransportePublico (String linea) {
// // System.out.println("nombre del bondi a buscar: "+ linea);
// //
// // System.out.println(transportesPublicos.get(1).getLinea().replace(" ",
// ""));

// TransportePublico busqueda = transportesPublicos.stream()
// .filter(transporte -> linea.equalsIgnoreCase(transporte.getLinea()))
// .findAny()
// .orElse(null);

// if(busqueda == null){
// throw new NoExisteException("No se encontro el Transporte especificado");
// }
// return busqueda;
// }

// public TransporteNoPublico getTransporteNoPublico (String nombre) {
// // System.out.println("nombre del transporte a buscar: "+ nombre);
// // System.out.println(transportesNoPublicos.get(0).getNombre() + "\n"
// +transportesNoPublicos.get(1).getNombre());
// TransporteNoPublico busqueda = transportesNoPublicos.stream()
// .filter(transporte -> nombre.replace(" ",
// "").equalsIgnoreCase(transporte.getNombre()))
// .findAny()
// .orElse(null);

// if(busqueda == null){
// throw new NoExisteException("No se encontro el Transporte especificado para:
// " + nombre);
// }
// return busqueda;
// }

// public void addTransporteNoPublico (TransporteNoPublico transporte) {
// transportesNoPublicos.add(transporte);
// }
// public void addTransportePublico (TransportePublico transporte) {
// transportesPublicos.add(transporte);
// }

// public boolean existeTransporteNoPublico (String nombreTransporte) {
// return
// Arrays.asList(transportesNoPublicos.stream().map(TransporteNoPublico::getNombre)).contains(nombreTransporte);
// }

// public boolean existeTransportePublico (String linea) {
// return
// Arrays.asList(transportesPublicos.stream().map(TransportePublico::getLinea)).contains(linea);
// }
// }
