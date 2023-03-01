package dds2022.grupo1.HuellaDeCarbono.services;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Adaptador;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.GeorefService;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.*;

import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ServicioGeoref implements Adaptador{
    private static ServicioGeoref instancia = null;
    private static int maximaCantidadRegistrosDefault = 200;
    private static final String urlApi = "https://ddstpa.com.ar/api/";
    private static final String apiToken = "Bearer laBt/w7PKokDfYFSbtuuU0VVQuxLJ+9yVa45zFv1tAc=";
    private static final int offset = 1;
    private Retrofit retrofit;

    public ServicioGeoref() {

            this.retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static ServicioGeoref getInstancia() {
        if (instancia == null) {
            instancia = new ServicioGeoref();
        }
        return instancia;
    }

    public List<Provincia> listadoDeProvincias(int offsetProvincia){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<List<Provincia>> requestProvinciasArgentinas = georefService.provincias(offsetProvincia, apiToken);
        try{
            Response<List<Provincia>> responseProvinciasArgentinas = requestProvinciasArgentinas.execute();
            return responseProvinciasArgentinas.body();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

    }

    public List<Municipio> listadoDeMunicipiosDeProvincia(int idProvincia, int offsetMunicipio){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<List<Municipio>> requestListadoDeMunicipios = georefService.municipios(idProvincia, offsetMunicipio, apiToken);
        try{
            Response<List<Municipio>> responseListadoDeMunicipios = requestListadoDeMunicipios.execute();
            return responseListadoDeMunicipios.body();
        }catch(IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Localidad> listadoDeLocalidadesDeMunicipio(int idMunicipio, int offsetLocalidad){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<List<Localidad>> requestListadoDeLocalidades = georefService.localidades(idMunicipio, offsetLocalidad, apiToken); //TODO: parametros completar
        try{
            Response<List<Localidad>> responseListadoDeLocalidades = requestListadoDeLocalidades.execute();
            return responseListadoDeLocalidades.body();
        }catch(IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public float distancia(Ubicacion ubicacionActual, Ubicacion ubicacionLlegada) {
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        int provinciaActualId = obtenerIdDeProvincia(ubicacionActual.getProvincia().toUpperCase(Locale.ROOT));
        int provinciaLlegadaId = obtenerIdDeProvincia(ubicacionLlegada.getProvincia().toUpperCase(Locale.ROOT));

        int municipioActualId = obtenerIdDeMunicipio(provinciaActualId, ubicacionActual.getMunicipio().toUpperCase(Locale.ROOT));
        int municipioLlegadaId = obtenerIdDeMunicipio(provinciaLlegadaId, ubicacionLlegada.getMunicipio().toUpperCase(Locale.ROOT));

        int localidadActualId = obtenerIdDeLocalidad(municipioActualId, ubicacionActual.getLocalidad().toUpperCase(Locale.ROOT));
        int localidadLlegadaId = obtenerIdDeLocalidad(municipioLlegadaId, ubicacionLlegada.getLocalidad().toUpperCase(Locale.ROOT));

        String calleActual = ubicacionActual.getDireccion();
        int alturaActual = Integer.parseInt(ubicacionActual.getAltura());
        String calleLlegada = ubicacionLlegada.getDireccion();
        int alturaLlegada = Integer.parseInt(ubicacionLlegada.getAltura());

        Call<Distancia> requestDistanciaEntreUbicaciones = georefService.distancia(localidadActualId, calleActual, alturaActual, localidadLlegadaId, calleLlegada, alturaLlegada, apiToken);
        try {
            Response<Distancia> responseDistanciaEntreUbicaciones = requestDistanciaEntreUbicaciones.execute();
            return (float) responseDistanciaEntreUbicaciones.body().getValor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int obtenerIdDeProvincia(String nombreProvincia) {
        int provinciaId = -1;
        int offsetProvincia = 1;
        do{
            List<Provincia> listadoDeProvinciasArgentinas = listadoDeProvincias(offsetProvincia);
            int cantidad = listadoDeProvinciasArgentinas.size();

            if(cantidad == 0){
                return 0;
                //TODO: VER DE PONER EXCEPTION
            }

            for(int i = 0; i < cantidad; i++){
                Provincia provincia = listadoDeProvinciasArgentinas.get(i);
                if(provincia.getNombre().equals(nombreProvincia)){
                    provinciaId = provincia.getId();
                }
            }
            offsetProvincia = offsetProvincia + 1;
        }while (provinciaId < 0);
        return provinciaId;
    }

    private int obtenerIdDeMunicipio(int idProvincia, String nombreMunicipio){
        int municipioId = -1;
        int offsetMunicipio = 1;
        do{
            List<Municipio> municipiosDeLaProvincia = listadoDeMunicipiosDeProvincia(idProvincia, offsetMunicipio);
            int cantidad = municipiosDeLaProvincia.size();

            if(cantidad == 0){
                return 0;
                //TODO: VER DE PONER EXCEPTION
            }

            for(int i = 0; i < cantidad; i++){
                Municipio municipio = municipiosDeLaProvincia.get(i);
                if(municipio.getNombre().equals(nombreMunicipio)){
                    municipioId = municipio.getId();
                }
            }
            offsetMunicipio = offsetMunicipio + 1;
        }while (municipioId < 0);

        return municipioId;
    }

    private int obtenerIdDeLocalidad(int idMunicipio, String nombreLocalidad){
        int localidadId = -1;
        int offsetLocalidad = 1;
        do{
            List<Localidad> localidadDelMunicipio = listadoDeLocalidadesDeMunicipio(idMunicipio, offsetLocalidad);
            int cantidad = localidadDelMunicipio.size();

            if(cantidad == 0){
                return 0;
                //TODO: VER DE PONER EXCEPTION
            }

            for(int i = 0; i < cantidad; i++){
                Localidad localidad = localidadDelMunicipio.get(i);
                if(localidad.getNombre().equals(nombreLocalidad)){
                    localidadId = localidad.getId();
                }
            }
            offsetLocalidad = offsetLocalidad + 1;
        }while (localidadId < 0);

        return localidadId;
    }


}
