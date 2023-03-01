package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;

import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import java.util.List;

public class TrayectoRepositorio extends RepositorioBase<Trayecto> {
    private static TrayectoRepositorio instancia = new TrayectoRepositorio();

    public TrayectoRepositorio() {
        super(Trayecto.class);
    }

    public static TrayectoRepositorio getInstancia() {
        return instancia;
    }

    public List<Trayecto> getById(Integer id) {
        Query query = entityManager.createNamedQuery("find trayecto by id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    public List<Trayecto> getTrayectosDelMiembro(Integer id) {
        Query query = entityManager.createNamedQuery("find trayectos by member");
        query.setParameter("miembro_id", id);
        return query.getResultList();
    }

    public void hacerModificacionDelTrayecto(Trayecto trayecto) {
        Query query = entityManager.createNamedQuery("editar trayecto");
        query.setParameter("id", trayecto.id);
        query.setParameter("ubicacionSalida", trayecto.getSalida());
        query.setParameter("ubicacionDestino", trayecto.getDestino());
        query.setParameter("transporte", trayecto.getMedioDeTransporte());
        query.setParameter("cantidadParticipantes", trayecto.getCantidadParticipantes());
        try {
            this.update(trayecto);
        } catch (TransactionRequiredException e) {
            System.out.println("ERROR");
        }
    }
}
