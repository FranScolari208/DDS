
package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;

import javax.persistence.Query;

public class MiembroRepositorio extends RepositorioBase<Miembro> {
    private static MiembroRepositorio instancia = new MiembroRepositorio();

    public MiembroRepositorio() {
        super(Miembro.class);
    }

    public static MiembroRepositorio getInstancia() {
        return instancia;
    }

    public Miembro getMiembro(Long dni) {
        Map<String, Number> valueDni = new HashMap<>();
        valueDni.put("dni", dni);
        return instancia.getByNumberValues(valueDni, false);
    }

    public List<Miembro> obtenerMiembroPorId(int idMiembro) {
        Query query = entityManager.createNamedQuery("find miembro by id");
        query.setParameter("id", idMiembro);
        return query.getResultList();
    }

    public List<Trayecto> getTrayectosDelMiembro(Miembro miembro){
        List<Trayecto> trayectosMiembro = new ArrayList<>();
        Query query = entityManager.createNamedQuery("getAllTrayecto");
        List<Trayecto> todosLosTrayectos = query.getResultList();
        for(int i =0; i < todosLosTrayectos.size(); i++){
            Trayecto trayectoActual = todosLosTrayectos.get(i);
            List<Miembro> miembrosTrayecto = trayectoActual.getMiembros();
            for(int j =0; j < miembrosTrayecto.size(); j++){
                if(miembro.getDni().equals(miembrosTrayecto.get(j).getDni())){
                    trayectosMiembro.add(trayectoActual);
                }
            }
        }
        return trayectosMiembro;
    }
    @Override
    protected Miembro handleSaveDuplicated(Miembro obj) {
        return getBy("dni", obj.getDni());
    }

}
