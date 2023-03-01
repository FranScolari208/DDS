package dds2022.grupo1.HuellaDeCarbono.services.utils;

import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;

import java.util.List;
import java.util.stream.Collectors;

public class miembro_utils {
    static public List<Trayecto> filterTrayectosPeriodo(List<Trayecto> trayectos, PeriodoDeImputacion periodo){
        return trayectos.stream().filter(
                trayecto -> trayecto.getPeriodoDeImputacion().esMismoPeriodo(periodo)
        ).collect(Collectors.toList());
    }

    static public List<Trayecto> filterTrayectoPorsAnioPeriodo(List<Trayecto> trayectos, PeriodoDeImputacion periodo){
        return trayectos.stream().filter(
                trayecto -> trayecto.getPeriodoDeImputacion().esMismoAnio(periodo)
        ).collect(Collectors.toList());
    }

    static public List<Trayecto> filterTrayectosOrganizacion(List<Trayecto> trayectos, Organizacion org) {
//        for(Trayecto trash : trayectos){
//            System.out.println("comienza loop");
//            System.out.println(trash.getSalida().getLatitud() + trash.getSalida().getLongitud());
//            System.out.println(trash.getDestino().getLatitud() + trash.getDestino().getLongitud());
//            System.out.println(org.getUbicacion().getLatitud() + org.getUbicacion().getLongitud());
//
//            System.out.println(trash.getSalida().esMismaUbicacion(org.getUbicacion()));
//            System.out.println(trash.getDestino().esMismaUbicacion(org.getUbicacion()));
//        }
        if (trayectos.stream().filter(
                trayecto ->
                        trayecto.getSalida().esMismaUbicacion(org.getUbicacion())
                                ||
                                trayecto.getDestino().esMismaUbicacion(org.getUbicacion())
        ).collect(Collectors.toList()).size() < 1){
            System.out.println(String.format("org: %s no tiene trayectos",org.getRazon_social()));
        }
        return trayectos.stream().filter(
                trayecto ->
                        trayecto.getSalida().esMismaUbicacion(org.getUbicacion())
                                ||
                                trayecto.getDestino().esMismaUbicacion(org.getUbicacion())
        ).collect(Collectors.toList());
    }

}
