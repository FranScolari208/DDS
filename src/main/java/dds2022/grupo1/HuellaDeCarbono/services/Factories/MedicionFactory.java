package dds2022.grupo1.HuellaDeCarbono.services.Factories;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.ActividadRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.enums.Mes;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoExisteException;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Medicion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.enums.Periodicidad;

import java.util.List;

public class MedicionFactory {
    public static Medicion getMedicion(List<String> listaMedicion) throws NoExisteException {
        Actividad actividad = ActividadRepositorio.getInstancia().getActividad(listaMedicion.get(0));
        TipoConsumo tipoConsumo = TipoConsumoRepositorio.getInstancia().getTipoConsumo(listaMedicion.get(1));
        Float valor = Float.parseFloat(listaMedicion.get(2));
        Periodicidad periodicidad = Periodicidad.valueOf(listaMedicion.get(3).toUpperCase());
        Consumo consumo = new Consumo(tipoConsumo, valor, periodicidad);
        PeriodoDeImputacion periodoDeImputacion;
        int fecha=Integer.parseInt(listaMedicion.get(4));
        if ( listaMedicion.get(4).length()> 4)
        {

            periodoDeImputacion=new PeriodoDeImputacion(Mes.getMesPorNumero(Integer.parseInt(listaMedicion.get(4).substring(4,6))),fecha/100);
        }
        else {

            periodoDeImputacion=new PeriodoDeImputacion(fecha/10);
        }

        return new Medicion(actividad, consumo, periodoDeImputacion);
    }
}
