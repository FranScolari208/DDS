package dds2022.grupo1.HuellaDeCarbono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.OrganizacionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.RepositorioBase;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;

public class RepoTest {

    @Ignore
    public void getearOrgPorIdNombre() {
        RepositorioBase<Organizacion> repoOrg = OrganizacionRepositorio.getInstancia();
        List<Organizacion> orgs = repoOrg.list();
        Organizacion primeraOrg = orgs.get(0);
        Long idPrimeraOrg = primeraOrg.getId();
        String primeraOrgNombre = primeraOrg.getRazon_social();
        Map<String, String> queryValues = new HashMap<>();
        queryValues.put("razon_social", primeraOrgNombre);
        System.out.println(primeraOrgNombre);

        Organizacion orgPorNombre = repoOrg.getByStringValues(queryValues, false);
        System.out.println(orgPorNombre.getRazon_social());
        Map<String, Number> otrosQuery = new HashMap<>();
        otrosQuery.put("id", 7);
        Organizacion orgPornumero = repoOrg.getByNumberValues(otrosQuery, false);
        System.out.println(orgPornumero.getRazon_social());
        // System.out.println(primeraOrgNombre);

    }
}
