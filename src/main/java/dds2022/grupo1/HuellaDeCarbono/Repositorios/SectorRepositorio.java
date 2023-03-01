package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;

import javax.persistence.Query;
import java.util.List;

public class SectorRepositorio extends RepositorioBase<Sector> {
    private static SectorRepositorio instancia = new SectorRepositorio();

    public SectorRepositorio() {
        super(Sector.class);
    }

    public static SectorRepositorio getInstancia() {
        return instancia;
    }

    public List<Sector> findSector(Organizacion id) {
        Query query = entityManager.createNamedQuery("find sector id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    public Sector obtenerSectorPorId(Long sectorId) {
        Query query = entityManager.createNamedQuery("find sector por id");
        query.setParameter("id", sectorId);
        return (Sector) query.getResultList().get(0);
    }
}
