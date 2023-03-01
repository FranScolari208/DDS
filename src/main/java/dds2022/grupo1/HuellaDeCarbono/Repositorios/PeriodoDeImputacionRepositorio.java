package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Mes;

import javax.persistence.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeriodoDeImputacionRepositorio extends RepositorioBase<PeriodoDeImputacion> {
    private static PeriodoDeImputacionRepositorio instancia = new PeriodoDeImputacionRepositorio();

    public PeriodoDeImputacionRepositorio() {
        super(PeriodoDeImputacion.class);
    }

    public static PeriodoDeImputacionRepositorio getInstancia() {
        return instancia;
    }

    public List<PeriodoDeImputacion> findWithAnioAndMes(Integer anio, Mes mes) {
        Query query = entityManager.createNamedQuery("find periodo by anio and mes");
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        return query.getResultList();
    }

    public PeriodoDeImputacion findWithAnioAndMeUno(Integer anio, Mes mes) {
        Query query = entityManager.createNamedQuery("find periodo by anio and mes");
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        return (PeriodoDeImputacion) query.getSingleResult();
    }


    @Override
    protected PeriodoDeImputacion handleSaveDuplicated(PeriodoDeImputacion obj) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mes", "'" + obj.getMes().toString() + "'");
        String anio = String.valueOf(obj.getAnio());
        params.put("anio", anio);
        return getByValuesCustom(params);
    }
}
