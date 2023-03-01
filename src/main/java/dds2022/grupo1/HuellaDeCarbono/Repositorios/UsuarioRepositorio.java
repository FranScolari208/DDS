package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Usuario;

import javax.persistence.Query;
import java.util.List;

public class UsuarioRepositorio extends RepositorioBase<Usuario>{
    // private Class<Usuario> type;

    private static UsuarioRepositorio instancia = new UsuarioRepositorio();

    public UsuarioRepositorio() {
        super(Usuario.class);
    }

    public static UsuarioRepositorio getInstancia() {
        return instancia;
    }


    // public Boolean existe(String nombreUsuario)  {
    //     return buscarUsuario(nombreUsuario) != null;
    // }
    // public Usuario buscarUsuario(String mail)
    // {
    //     return EntityManagerHelper.getEntityManager().find(type, mail);
    // }

    public Usuario findUsuario (String email){
        Query query = entityManager.createNamedQuery("find usuario id");
        query.setParameter("emailUser", email);
        return (Usuario) query.getSingleResult();
    }
    public String findUsuarioContrasenia (String email){
        Query query = entityManager.createNamedQuery("find usuario contrasenia");
        query.setParameter("emailUser", email);
        return (String) query.getSingleResult();
    }


    public List<Organizacion> findOrganizaciones (String email){
        Query query = entityManager.createNamedQuery("find usuario organizaciones");
        query.setParameter("emailUser", email);
        return  query.getResultList();
    }
    /*@Override
    public List<Unidad> buscarTodos() {
        return null;
    }



    @Override
    public void agregar(Object unObjeto) {

    }

    @Override
    public void modificar(Object unObjeto) {

    }

    @Override
    public void eliminar(Object unObjeto) {

    }*/
}
