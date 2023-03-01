package dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

import javax.persistence.*;

@Table
@Entity
@NamedQuery(name = "getAllMiembroPorSector", query = "select a from MiembroPorSector a")
@NamedNativeQuery(name = "truncateMiembroPorSector", query = "truncate table MiembroPorSector")
// @NamedQuery(query = "Select s.miembro from MiembroPorSector s where s.sector
// = :id", name = "find miembro by sector id")
public class MiembroPorSector extends Persistente {

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "miembro_id", referencedColumnName = "id")
    private Miembro miembro;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id", referencedColumnName = "id")
    private Sector sector;
    @Column
    private boolean aceptado;

    public MiembroPorSector() {
    }

    public MiembroPorSector(Miembro miembro, Sector sector, boolean aceptado) {
        this.miembro = miembro;
        this.sector = sector;
        this.aceptado = aceptado;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setStatus(boolean estado) {
        aceptado = estado;
    }

    public float calcularHCTrayectoCompartido(Organizacion organizacion) throws NoExisteException {
        return (float) miembro.getTrayectosOrganizacion(organizacion).stream()
                .mapToDouble(Trayecto::calcularHCDivididoParticipantes).sum();
    }
}
