package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.MiembroPorSector;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

import java.util.ArrayList;
import java.util.List;

public class MiembroPorSectorRepositorio extends RepositorioBase<MiembroPorSector> {
    private List<MiembroPorSector> miembroPorSectores = new ArrayList<MiembroPorSector>();
    private static MiembroPorSectorRepositorio instancia = new MiembroPorSectorRepositorio();

    public MiembroPorSectorRepositorio() {
        super(MiembroPorSector.class);
    }

    public static MiembroPorSectorRepositorio getInstancia() {
        return instancia;
    }

    public MiembroPorSector getMiembroPorSector(int id) throws NoExisteException {
        MiembroPorSector busqueda = miembroPorSectores.stream()
                .filter(consumo -> id == consumo.getId())
                .findAny()
                .orElse(null);

        if (busqueda == null) {
            throw new NoExisteException("No se encontro el miembro por sector");
        }
        return busqueda;
    }

    public MiembroPorSector getMiembroPorSector(Miembro miembro) throws NoExisteException {
        MiembroPorSector busqueda = miembroPorSectores.stream()
                .filter(miembroPorSector -> miembro == miembroPorSector.getMiembro())
                .findAny()
                .orElse(null);

        if (busqueda == null) {
            throw new NoExisteException("No se encontro el miembro por sector");
        }
        return busqueda;
    }

    public MiembroPorSector getMiembroPorSector(Sector sector) throws NoExisteException {
        MiembroPorSector busqueda = miembroPorSectores.stream()
                .filter(miembroPorSector -> sector == miembroPorSector.getSector())
                .findAny()
                .orElse(null);

        if (busqueda == null) {
            throw new NoExisteException("No se encontro el miembro por sector");
        }
        return busqueda;
    }

    public void addMedicion(MiembroPorSector miembroPorSector) {
        this.miembroPorSectores.add(miembroPorSector);
    }

    public List<MiembroPorSector> getMiembroPorSectores() {
        return miembroPorSectores;
    }

    public void setMiembroPorSectores(List<MiembroPorSector> miembroPorSectores) {
        this.miembroPorSectores = miembroPorSectores;
    }

    public void removeMiembroPorSector(MiembroPorSector miembroPorSector) {
        this.miembroPorSectores.remove(miembroPorSector);
    }

    /*
     * public List<Miembro> findMiembroWithId (Sector id){
     * Query query = entityManager.createNamedQuery("find miembro by sector id");
     * query.setParameter("id", id);
     * return query.getResultList();
     * }
     */

}