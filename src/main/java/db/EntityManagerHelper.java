package db;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityManagerHelper {

    private static EntityManagerFactory emf;

    private static ThreadLocal<EntityManager> threadLocal;

    /*static {
        try {
            emf = Persistence.createEntityManagerFactory("db");
            threadLocal = new ThreadLocal<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static EntityManagerFactory startEntityManagerFactory() throws URISyntaxException {
        //https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<String, Object>();

        String[] keys = new String[]{
                //, "javax.persistence.jdbc.url",
                //"javax.persistence.jdbc.user", "javax.persistence.jdbc.password",
                "DATABASE_URL",
                "hibernate.show_sql",
                "ddlauto",
                "javax.persistence.jdbc.driver"
                //"javax.persistence.schema-generation.database.action"
        };

        for (String key : keys) {
            if (env.containsKey(key)) {

                if (key.equals("DATABASE_URL1")) {
                    // https://devcenter.heroku.com/articles/connecting-heroku-postgres#connecting-in-java
                    String value = env.get(key);

                    URI dbUri = new URI(value);
                    System.out.println(value);
                    String username = dbUri.getUserInfo().split(":")[0];
                    String password = dbUri.getUserInfo().split(":")[1];
                    //javax.persistence.jdbc.url=jdbc:postgresql://localhost/dblibros
                    value = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();// + "?sslmode=require";
                    configOverrides.put("javax.persistence.jdbc.url", value);
                    configOverrides.put("javax.persistence.jdbc.user", username);
                    configOverrides.put("javax.persistence.jdbc.password", password);
                    //  configOverrides.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

                }
                if (key.equals("ddlauto")) {
                    String value = env.get(key);
                    configOverrides.put("javax.persistence.schema-generation.database.action", value);
                }


            }
        }

        return Persistence.createEntityManagerFactory("db", configOverrides);

    }

    static {
        try {
            emf = startEntityManagerFactory();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private static EntityManagerFactory emf() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("db");
        }
        return emf;
    }

    private static ThreadLocal<EntityManager> threadLocal() {
        if (threadLocal == null) {
            threadLocal = new ThreadLocal<>();
        }
        return threadLocal;
    }

    public static EntityManager entityManager() {
        return getEntityManager();
    }

    public static EntityManager getEntityManager() {
        EntityManager manager = threadLocal().get();
        if (manager == null || !manager.isOpen()) {
            manager = emf().createEntityManager();
            threadLocal().set(manager);
        }
        return manager;
    }

    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if (em != null) {
            threadLocal.set(null);
            em.close();
        }
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }

    public static void beginTransaction() {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        if (!tx.isActive()) {
            tx.begin();
        }
    }

    public static void commit() throws RollbackException {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.commit();
        }

    }

    public static void rollback() {
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
    }

    public static Query createQuery(String query) {
        return getEntityManager().createQuery(query);
    }

    public void close() {
        closeEntityManager();
    }

    public static void persist(Object o) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        beginTransaction();
        try {
            em.persist(o);
            commit();
        } catch (EntityExistsException e) {
            rollback();
            throw e;
        }
        // catch (PersistenceException e) {
        // rollback();
        // em.merge(o);
        // commit();
        // }
        finally {
            // closeEntityManager();
        }
    }

    public static void update(Object o) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        beginTransaction();
        try {
            em.merge(o);
            commit();

        } catch (Exception e) {
            rollback();
            throw e;
        } finally {
            // closeEntityManager();
        }
    }

    public static void delete(Object o) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        beginTransaction();
        try {
            em.remove(o);
            commit();
        } catch (IllegalArgumentException e) {
            rollback();
            throw new NoExisteException("No existe el obj que se quiere borrar");
        } finally {
            // closeEntityManager();
        }

    }

    public static <T> void truncate(Class<T> clase) throws IOException {
        EntityManager em = EntityManagerHelper.getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(clase);
        query.from(clase);
        beginTransaction();
        try {
            em.createQuery(query).executeUpdate();
            commit();

        } catch (Exception e) {
            rollback();
            throw new IOException("Fallo el truncate para " + clase.getName());
        } finally {
            // closeEntityManager();
        }
    }

    public static void withTransaction(Runnable action) {
        withTransaction(() -> {
            action.run();
            return null;
        });
    }

    public static <A> A withTransaction(Supplier<A> action) {
        beginTransaction();
        try {
            A result = action.get();
            commit();
            return result;
        } catch (Throwable e) {
            rollback();
            throw e;
        }
    }
}