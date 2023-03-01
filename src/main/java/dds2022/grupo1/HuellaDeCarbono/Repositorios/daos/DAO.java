package dds2022.grupo1.HuellaDeCarbono.Repositorios.daos;



import java.util.List;

public interface DAO<T> {
    List<T> buscarTodos();
    T buscar(int id);
    //T buscar(BusquedaCondicional busquedaCondicional);
    void agregar(Object unObjeto);
    void modificar(Object unObjeto);
    void eliminar(Object unObjeto);
}
