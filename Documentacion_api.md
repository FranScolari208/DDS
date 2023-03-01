DOCUMENTACION API

Nuestro dominio en este caso es: https://equipo1.vps2.disilab.cpci.org.ar/

Cuando se vea escrito "nombreModelo" aplica para las siguientes expresiones:

    {
        Organizacion: "api/orgs",
        Agente: "api/agentes",
        Medicion: "api/mediciones",
        Trayecto: "api/trayectos",
    }

Estos metodos funcionan igual para todos los controladores que tienen CRUD Completo o al menos ABM: 
    
    GET: www.nuestroDominio.com/{nombreModelo}/{pk}
        -> retorna el objeto buscado en formato JSON (no retorna valores relacionados, es decir listas de relaciones etc.)
    
    GET: www.nuestroDominio.com/{nombreModelo}
        -> retorna todos los objetos de ese modelo con formato JSON
    
    DELETE: www.nuestroDominio.com/{nombreModelo}/{pk}
        -> borra el objeto con el pk suministrado a la api (no hay opciones de seguridad para esta accion asi que con cuidado)

Tipos de Datos:[

    Ubicacion: {
        latitud: Float,
        longitud: Float,
        altura: String,
        municipio: String,
        direccion: String,
        localidad: String,
        provincia: String
    },
    Tramo: {
        actual: Ubicacion,
        siguiente: Ubicacion,
        anterior?: Ubicacion
    },
    Organizacion: {
        id?: int,
        razonSocial: String,
        ubicacion: Ubicacion,
        clasificacion: String,
        tipoOrganizacion: String
    },
    Trayecto: {
        id: int,
        destino: Ubicacion,
        salida: Ubicacion,
        medioTransporte: List<String> => [String] || [String, String]
        // ejemplos medioTransporte: ["bicicleta"] || ["auto", "NAFTA"]|| ['colectivo', 'Linea xx']
        imputacion: String,
        tramos: List<Tramo>,
        participantes: int,
        miembros: List<int||long> (lista dnis o ids) a definir
    },
    Agente: {
        id?: int,
        sectorId: int,
        nombre: String,
        telefono: String,
        email: String,
    }
]


Organizacion:

    POST www.nuestroDominio.com/api/orgs
        body: Organizacion
        -> retorna mensaje de error o success :D

    POST www.nuestroDominio.com/orgs/asignar_miembro?sectorId={sectId}&miembroId={miemId}
        -> retorna success o fail

    PUT www.nuestroDominio.com/api/orgs/{pk}
        body: Organizacion
        -> retorna success o fail si se inserta o no.


Sector:
        
        POST www.nuestroDominio.com/api/orgs/asignar_sector
        body:  {
                "nombre": "nombre del sector",
                "organizacionId": "nroDeID"
                }
        -> retorna mensaje de error o success :D

Trayecto:

    POST www.nuestroDominio.com/api/trayectos
        body: Trayecto
        -> success o fail

    PUT www.nuestroDominio.com/api/trayectos/{pk}
        body: Trayecto
        -> success o fail

Agente:

    POST www.nuestroDominio.com/api/agentes
        body: Agente
    -> success o fail

    PUT www.nuestroDominio.com/api/agentes/{pk}
        body: Agente
    -> success o fail

Mediciones:
    
    POST www.nuestroDominio.com/api/batch?org_id={orgId}&sect_id={sectId}
        body: un archivo en formato "form" con el nombre "uploaded_file"
        -> success o fail

    *FILTROS*
    GET: www.nuestroDominio.com/api/mediciones?razon_social={razonSocial}&anio={anio}&mes={mes}
        queryParams: la razon social puede ir o no, de igual forma que el anio, pero si esta el mes debe venir acompañado del año.
    CHEQUEAR ESTO ULTIMO!!!
