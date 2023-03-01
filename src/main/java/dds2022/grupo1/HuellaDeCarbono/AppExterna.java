package dds2022.grupo1.HuellaDeCarbono;

import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.*;
import dds2022.grupo1.HuellaDeCarbono.services.ServicioGeoref;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AppExterna {
    public static void verQueViene(Request req, Response res){
        System.out.println(req.params());
}
    public static void main(String[] args) throws IOException{
/*        Organizacion org1 = new Organizacion("Coca", new TipoOrganizacion("ni idea"), new Ubicacion(1.2f,3.4f), Clasificacion.CAPITAL_ABIERTO);
        OrganizacionRepositorio repo = OrganizacionRepositorio.getInstancia();
        repo.addOrganizacion(org1);
//        user -> clase
//        userService -> repository
//        controller -> la clase que maneja los endpoints de un modelo
        get("/hello/:", (req, res) -> req.params());
        new ControladorOrganizacion(OrganizacionRepositorio.getInstancia());
*/
        ServicioGeoref servicioGeoref = ServicioGeoref.getInstancia();
        System.out.println("Seleccione una de las siguientes provincias, ingresando su id:");

        List<Provincia> listadoDeProvinciasArgentinas = servicioGeoref.listadoDeProvincias(1);

        listadoDeProvinciasArgentinas.sort((p1, p2) -> p1.getId() >= p2.getId()? 1 : -1);

        for(Provincia unaProvincia:listadoDeProvinciasArgentinas){
            System.out.println(unaProvincia.getId() + ") " + unaProvincia.getNombre());
        }

        Scanner entradaScanner = new Scanner(System.in);
        int idProvinciaElegida = Integer.parseInt(entradaScanner.nextLine());

        Optional<Provincia> posibleProvincia = provinciaDeId(listadoDeProvinciasArgentinas, idProvinciaElegida);

        if(posibleProvincia.isPresent()){
            Provincia provinciaSeleccionada = posibleProvincia.get();
            List<Municipio> municipiosDeLaProvincia = servicioGeoref.listadoDeMunicipiosDeProvincia(provinciaSeleccionada.getId(), 1);
            System.out.println("Los municipios de la provincia "+ provinciaSeleccionada.getNombre() +" son:");
            for(Municipio unMunicipio: municipiosDeLaProvincia){
                System.out.println(unMunicipio.getId() + ") " + unMunicipio.getNombre());
            }

            int idMunicipioElegido = Integer.parseInt(entradaScanner.nextLine());

            Optional<Municipio> posibleMunicipio = municipioDeId(municipiosDeLaProvincia, idMunicipioElegido);

            if(posibleMunicipio.isPresent()){
                Municipio municipioSeleccionado = posibleMunicipio.get();
                List<Localidad> localidadesDelMunicipio = servicioGeoref.listadoDeLocalidadesDeMunicipio(municipioSeleccionado.getId(), 1);
                System.out.println("Las localidades del municipio "+ municipioSeleccionado.getNombre() +" son:");
                for(Localidad unaLocalidad: localidadesDelMunicipio){
                    System.out.println(unaLocalidad.getId() + ") " + unaLocalidad.getNombre());
                }
            }
            else{
                System.out.println("No existe el municipio seleccionado");
            }
        }
        else{
            System.out.println("No existe la provincia seleccionada");
        }
    }

    private static Optional<Municipio> municipioDeId(List<Municipio> municipiosDeLaProvincia, int idMunicipioElegido) {
        return municipiosDeLaProvincia.stream()
                .filter(m -> m.getId() == idMunicipioElegido)
                .findFirst();
    }

    private static Optional<Provincia> provinciaDeId(List<Provincia> listadoDeProvinciasArgentinas, int idProvinciaElegida) {
        return listadoDeProvinciasArgentinas.stream()
                .filter(p -> p.getId() == idProvinciaElegida)
                .findFirst();
    }


}
