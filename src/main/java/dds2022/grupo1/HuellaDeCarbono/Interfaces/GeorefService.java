package dds2022.grupo1.HuellaDeCarbono.Interfaces;

import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import java.util.List;


public interface GeorefService {
    @GET("provincias")
    Call<List<Provincia>> provincias(@Header("Authorization") String token);

    @GET("provincias")
    Call<List<Provincia>> provincias(@Query("offset") int offset, @Header("Authorization") String token);

    @GET("municipios")
    Call<List<Municipio>> municipios(@Query("provinciaId") int idProvincia, @Header("Authorization") String token);

    @GET("municipios")
    Call<List<Municipio>> municipios(@Query("provinciaId") int idProvincia, @Query("offset") int offset, @Header("Authorization") String token);

    @GET("municipios")
    Call<List<Municipio>> municipios(@Query("provinciaId") int idProvincia, @Query("campos") String campos, @Query("max") int max, @Header("Authorization") String token);

    @GET("localidades")
    Call<List<Localidad>> localidades(@Query("municipioId") int idMunicipio, @Query("offset") int offset, @Header("Authorization") String token);

    @GET("distancia")
    Call<Distancia> distancia(@Query("distancia") String campos, @Header("Authorization") String token);

    @GET("distancia")
    Call<Distancia> distancia(@Query("localidadOrigenId") int localidadOrigenId, @Query("calleOrigen") String calleOrigen, @Query("alturaOrigen") int alturaOrigen, @Query("localidadDestinoId") int localidadDestinoId, @Query("calleDestino") String calleDestino, @Query("alturaDestino") int alturaDestino, @Header("Authorization") String token);
}
