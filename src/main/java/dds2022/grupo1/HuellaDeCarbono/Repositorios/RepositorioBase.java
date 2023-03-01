package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import db.EntityManagerHelper;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

public abstract class RepositorioBase<T> {
    // protected RepositorioBase<T> instancia = new RepositorioBase<T>();
    // protected Map<Class<T>, RepositorioBase<T>> instancias = new HashMap<>();
    public static EntityManager entityManager = EntityManagerHelper.entityManager();
    protected Class<T> clase;
    // protected List<String> uniqueConstraintValues;

    protected RepositorioBase(Class<T> clase) { // List<String> values) {
        this.clase = clase;
        // this.uniqueConstraintValues = values;
        // private to prevent anyone else from instantiating
    }

    protected RepositorioBase() {
    }

    public void setClase(Class<T> clase) {
        this.clase = clase;
    }

    protected String getClaseNombre() {
        String[] nombreSpliteado = clase.getName().split("[.]", 0);
        return nombreSpliteado[nombreSpliteado.length - 1];
    }

    protected T handleSaveDuplicated(T obj) {
        System.out.print("Handleando duplicados para el obj: "+obj.toString());
        return getByNombre(obj.toString());
    }

    public T save(T obj) {
        try {
            EntityManagerHelper.persist(obj);
            // update(obj);
            return obj;
        } catch (Exception e2) {
            try {
                System.out.println(e2);
                update(obj);
                return obj;
            } catch (RollbackException e) {
                System.out.print(e);
                return handleSaveDuplicated(obj);
            }
        }
    }

    public void update(T obj) {
        EntityManagerHelper.update(obj);
    }

    public void multiSave(List<T> objs) {
        for (T obj : objs) {
            save(obj);
        }
    }

    public T get(Number id) {
        try {
            return EntityManagerHelper.entityManager().find(clase, id);
        } catch (IllegalArgumentException e) {
            throw new NoExisteException(
                    "No se encontro el recurso para el id:" +
                            id +
                            " de la clase: " + clase.getName());
        }
    }

    public T getByNombre(String nombre) {
        return getBy("nombre", nombre);
    }

    private TypedQuery<T> executeNamedQuery(String nombre) {
        TypedQuery<T> query1 = entityManager.createNamedQuery(nombre, clase);
        return query1;
    }

    public List<T> list() {
        return executeNamedQuery(String.format("getAll%s", getClaseNombre())).getResultList();
    }

    public void truncate() throws IOException {
        EntityManagerHelper.truncate(clase);
    }

    public void delete(Long id) {
        // podriamos tirar una exception para que falle con mas gracia
        EntityManager em = EntityManagerHelper.getEntityManager();
        T obj = em.find(clase, id);
        EntityManagerHelper.delete(obj);
    }

    public void delete(int id) {
        // podriamos tirar una exception para que falle con mas gracia
        EntityManager em = EntityManagerHelper.getEntityManager();
        T obj = em.find(clase, id);
        EntityManagerHelper.delete(obj);
    }


    public T getByStringValues(Map<String, String> queryParams, boolean or) {
        /*
         * Nos llega un map y hacemos una query que devuelva un valor para
         * una busqueda con ORs o ANDs dependiendo el bool de inclusive
         */
        return getByValues(queryParams, or, false);
    }

    public T getByNumberValues(Map<String, Number> queryValues, boolean or) {
        /*
         * Nos llega un map y hacemos una query que devuelva un valor para
         * una busqueda con ORs o ANDs dependiendo el bool de inclusive
         */
        Map<String, String> castedMap = new HashMap<>();
        for (Map.Entry<String, Number> entry : queryValues.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
            castedMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return getByValues(castedMap, or, true);
    }

    public T getByValues(Map<String, String> queryParams, boolean or, boolean numerico) {
        /*
         * Nos llega un map y hacemos una query que devuelva un valor para
         * una busqueda con ORs o ANDs dependiendo el bool de inclusive
         * SOLO FUNCIONA CON STRINGS!!
         */
        String separador = or ? " OR " : " AND ";
        String queryValuesFormat = numerico ? "%s = %s" : "%s = '%s'";
        List<String> queryValuesList = new ArrayList<>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
            queryValuesList.add(String.format(queryValuesFormat, entry.getKey(), entry.getValue()));
        }
        String queryValues = String.join(separador, queryValuesList);
        String query = String.format("select a FROM %s a WHERE %s", getClaseNombre(), queryValues);

        Query q = entityManager.createQuery(query);
        System.out.println(query);
        /*try{
            T object = (T) q.getSingleResult();
            return object;
        }catch(){

        }
        return null;*/
        return (T) q.getSingleResult();
    }



    public T getByValuesCustom(Map<String, String> queryParams) {
        return (T) queryByValuesCustom(queryParams).getSingleResult();
    }
    public List<T> getManyByValuesCustom(Map<String, String> queryParams) {
        return  (List<T>) queryByValuesCustom(queryParams).getResultList();
    }



    private Query queryByValuesCustom(Map<String, String> queryParams) {
        String separador = " AND ";
        String queryValuesFormat =  "%s = %s";
        List<String> queryValuesList = new ArrayList<>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
            queryValuesList.add(String.format(queryValuesFormat, entry.getKey(), entry.getValue()));
        }
        String queryValues = String.join(separador, queryValuesList);
        String query = String.format("select a FROM %s a WHERE %s", getClaseNombre(), queryValues);

        return entityManager.createQuery(query);
    }

    public T getBy(String nombreColumna, String nombre) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(nombreColumna, nombre);
        return getByStringValues(map, false);
    }

    public T getBy(String nombreColumna, Number nombre) {
        Map<String, Number> map = new HashMap<String, Number>();
        map.put(nombreColumna, nombre);
        return getByNumberValues(map, false);
    }

    // public T getBy(String nombreColumna, int nombre) {
    // Map<String, Integer> map = new HashMap<String, Integer>();
    // map.put(nombreColumna, nombre);
    // return getByNumberValues(map, false);
    // }

}
