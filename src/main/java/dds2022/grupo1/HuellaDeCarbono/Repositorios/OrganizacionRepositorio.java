package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;

import javax.persistence.Query;
import java.util.List;

public class OrganizacionRepositorio extends RepositorioBase<Organizacion> {
    private static OrganizacionRepositorio instancia = new OrganizacionRepositorio();

    public OrganizacionRepositorio() {
        super(Organizacion.class);
    }

    public static OrganizacionRepositorio getInstancia() {
        return instancia;
    }

    @Override
    protected Organizacion handleSaveDuplicated(Organizacion obj) {
        return getBy("razon_social", obj.getRazon_social());
    }

    public List<Organizacion> getAllOrganizaciones() {
        Query query = entityManager.createNamedQuery("getAllOrganizacion");
        return query.getResultList();
    }

    public Organizacion getOrganizacion(Long id) {
        Query query = entityManager.createNamedQuery("getOrganizacion");
        query.setParameter("id", id);
        return (Organizacion) query.getResultList().get(0);
    }
}