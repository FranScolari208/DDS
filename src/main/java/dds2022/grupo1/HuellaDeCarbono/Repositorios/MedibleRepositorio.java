package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Medible;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;

import javax.persistence.Query;

import db.EntityManagerHelper;

import java.util.List;

public class MedibleRepositorio extends RepositorioBase<Medible> {
    private static MedibleRepositorio instancia = new MedibleRepositorio();

    public MedibleRepositorio() {
        super(Medible.class);
    }

    public static MedibleRepositorio getInstancia() {
        return instancia;
    }

    public List<Medible> findWithId(Organizacion id) {
        Query query = entityManager.createNamedQuery("find organizacion by id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    public List<Medible> findSectorWithId(Organizacion id) {
        Query query = entityManager.createNamedQuery("find sector by id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    protected Medible handleSaveDuplicated(Medible obj) {
        System.out.print("Handleando duplicados para el obj: "+obj.toString());
        return obj;
    }

    public Medible save(Medible obj) {
        EntityManagerHelper.persist(obj);
        return obj;
    }

}
