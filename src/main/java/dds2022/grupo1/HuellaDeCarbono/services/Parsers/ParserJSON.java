package dds2022.grupo1.HuellaDeCarbono.services.Parsers;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.MedioDeTransporte;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserDatosOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.ParserTransporte;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.MiembroRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Miembro;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.TipoOrganizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.Parada;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TipoTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteContratado;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteEcologico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransporteParticular;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransportePublico;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;
import dds2022.grupo1.HuellaDeCarbono.enums.Clasificacion;
import dds2022.grupo1.HuellaDeCarbono.enums.TipoDni;
import dds2022.grupo1.HuellaDeCarbono.exceptions.MiembroNoExisteException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParserJSON extends ParserAbs implements ParserDatosOrganizacion, ParserTransporte {
    JSONObject json;

    public ParserJSON (String nombreArchivo) throws IOException {
        super(nombreArchivo);
        leerArchivo();
         
    }

    @Override
    public void leerArchivo () throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(this.nombreArchivo)));
        json = new JSONObject(jsonString);
    }
    
    
    @Override
    public List<MedioDeTransporte> leerTransportes() throws Exception {            
        /*
            *FORMATO JSON TRANSPORTE PUBLICO
            "transportes":[
                {
                    "tipo": publico/ecologico/contratado/particular
                    opcional -> "combustible": NAFTA/DIESEL/etc
                    opcional -> "nombre": Auto, Camioneta, etc
                    "nombreLinea": "Linea 24",
                    "tipoVehiculo": "Colectivo", -> esto deberia ser una clase, hermano
                    "paradas":[
                        {"Lat":float, "Lon": Float, "distNext": Int, "distPrev": Int},
                        {"ubicacion": Ubicacion, "distNext": Int, "distPrev": Int},
                        ...;;;
                    ],
                }
            ]
        */
        
        List<MedioDeTransporte> listaTransportes = new ArrayList<>();

        JSONArray transportes = json.getJSONArray("transportes");
        for (int i = 0; i < transportes.length(); i++) {
            JSONObject transporte = transportes.getJSONObject(i);
            String tipo = transporte.getString("tipo").toLowerCase();
            switch(tipo){
                case "publico":
                    String linea = transporte.getString("linea");
                    String tipoPublico = transporte.getString("tipoVehiculo");
                    JSONArray paradas = transporte.getJSONArray("paradas");
                    TipoTransporte tipoTransporte = new TipoTransporte(tipoPublico);
                    listaTransportes.add(new TransportePublico(tipoTransporte, leerParadas(paradas), linea));
                    break;

                case "ecologico":
                    String tipoEcologico = transporte.getString("tipoVehiculo");
                    TipoTransporte tipoTransporteEco = new TipoTransporte(tipoEcologico);
                    listaTransportes.add(new TransporteEcologico(tipoTransporteEco));
                    break;

                case "particular":
                    String tipoParticular = transporte.getString("tipoVehiculo");
                    String combustible = transporte.getString("combustible");
                    TipoTransporte tipoTransportePartic = new TipoTransporte(tipoParticular);
                    listaTransportes.add(new TransporteParticular(tipoTransportePartic, combustible));
                    break;

                case "contratado":
                    String tipoContratado = transporte.getString("tipoVehiculo");
                    TipoTransporte tipoTransporteContr = new TipoTransporte(tipoContratado);
                    listaTransportes.add(new TransporteContratado(tipoTransporteContr));
                    break;

                default:
                    throw new Exception("No se selecciono un tipo de transporte correcto");
            }
           
        }
        return listaTransportes;
    }
    
    private List<Parada> leerParadas (JSONArray paradas) {
        List<Parada> listaParadas = new ArrayList<>();

        for (int i = 0; i < paradas.length(); i++) {
            JSONObject parada = paradas.getJSONObject(i);
            Float latitud = parada.getFloat("Lat");
            Float longitud = parada.getFloat("Lon");
            Float distanciaProx = parada.getFloat("distProxima");
            Float distanciaAnt = parada.getFloat("distAnterior");
            listaParadas.add(new Parada(new Ubicacion(latitud, longitud), distanciaAnt, distanciaProx));
        }
        return listaParadas;
    }


    // "Organizaciones":[ {
    //     "Razon Social": "Empresa ficticia 1",
    //      "Tipo Organizacion": {
    //         "descripcion": " una descripcion piola";
    //        },
    //      "Ubicacion": {
    //         "Direccion": "Direccion 123",
    //         "Latitud": "coordenada1",
    //         "Longitud": "coordenada2",
    //        },
    //       "Clasificacion": "una clasificacion"(es un enum no se si hay que ponerlo de otra forma),
    //       "Sectores": [
    //         {
    //             "Nombre": "Legales",
    //             "Miembros": [
    //                     {
    //                        "Nombre": "Carlos" ,
    //                         "Apellido": "Gardel",
    //                         "Tipo DNI ": "LIBRETA_CIVICA" (es un enum tambien),
    //                         "DNI": "1234567",
    //                       } ,
    //                     {
    //                        "Nombre": "Raul" ,
    //                         "Apellido": "Cascini",
    //                         "Tipo DNI ": "DNI"(es un enum tambien),
    //                         "DNI": "7891011",
    //                     } 
    //               ],             
    //         },
    //     ],
    // },]

    @Override
    public List<Organizacion> leerOrganizaciones() {
        List<Organizacion> organizaciones = new ArrayList<>();

        JSONArray organizacionesJSON = json.getJSONArray("Organizaciones");
        for (int i = 0; i < organizacionesJSON.length(); i++) {

            JSONObject organizacion = organizacionesJSON.getJSONObject(i);
            String razonSocial = organizacion.getString("Razon Social");
            String tipoOrganizacion = organizacion.getString("Tipo Organizacion");
            JSONObject ubicacion = organizacion.getJSONObject("Ubicacion");
            String clasificacion = organizacion.getString("Clasificacion");
            //tipo org deberia ser un nombre nomas? SA, etc
            //capaz buscar en un repo antes de hacerlo asi? o sino ENUM??
            TipoOrganizacion tipoOrg = new TipoOrganizacion(tipoOrganizacion);

            Ubicacion ubi = new Ubicacion(ubicacion.getFloat("Latitud"), ubicacion.getFloat("Longitud"));
            Clasificacion clasi = Clasificacion.valueOf(clasificacion.toUpperCase());
            
            Organizacion org = new Organizacion(razonSocial, tipoOrg, ubi, clasi);
            org.setSectores(this.leerSectores(organizacion, org));
            organizaciones.add(org);

        }
        return organizaciones;
    }
    
    public List<Sector> leerSectores(JSONObject organizacion, Organizacion org) {
        List<Sector> sectores = new ArrayList<>();
        JSONArray sectoresJSON = organizacion.getJSONArray("Sectores");
        for (int i = 0; i < sectoresJSON.length(); i++) {
            JSONObject sectorJSON = sectoresJSON.getJSONObject(i);
            String nombre = sectorJSON.getString("Nombre");
            JSONArray miembrosJSON = sectorJSON.getJSONArray("Miembros");
            //aceptados only
            Sector sector = new Sector(nombre, org);

            sector.setMiembrosAceptados(this.leerMiembros(miembrosJSON, sector));
            sectores.add(sector);
        }
        return sectores;
    }
    
    public List<Miembro> leerMiembros(JSONArray miembrosJSON, Sector sector) {
        List<Miembro> miembros = new ArrayList<>();
        for(int i = 0; i < miembrosJSON.length(); i++){
            JSONObject miembroJSON = miembrosJSON.getJSONObject(i);
            String nombre = miembroJSON.getString("Nombre");
            String apellido = miembroJSON.getString("Apellido");
            TipoDni tipoDni = TipoDni.valueOf(miembroJSON.getString("Tipo DNI"));
            Long dni = miembroJSON.getLong("DNI");
            
            Miembro miembro;
            try {
                // capaz alguna org comparte miembros y no los queremos duplicados
                miembro = MiembroRepositorio.getInstancia().getMiembro(dni);
            } catch (MiembroNoExisteException e) {
                miembro = new Miembro(nombre, apellido, tipoDni, dni);
            }

            miembro.addSector(sector);

            miembros.add(miembro);
        }
        return miembros;
    }
}
