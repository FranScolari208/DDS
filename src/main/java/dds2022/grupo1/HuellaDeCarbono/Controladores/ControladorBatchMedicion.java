package dds2022.grupo1.HuellaDeCarbono.Controladores;

import static spark.Spark.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.MultipartConfigElement;





import static dds2022.grupo1.HuellaDeCarbono.services.utils.JsonUtil.json;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.ActividadRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.BatchMedicionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.ConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.FactorEmisionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.MedibleRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.OrganizacionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.PeriodoDeImputacionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.SectorRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.TipoConsumoRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.UnidadRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Actividad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.BatchMedicion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Consumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.FactorEmision;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Medicion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.PeriodoDeImputacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.TipoConsumo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Medicion.Unidad;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Sector;
import dds2022.grupo1.HuellaDeCarbono.services.Parsers.ParserCSV;

public class ControladorBatchMedicion extends ControladorBase<BatchMedicion> {
    @Override
    public void setRepo() {
        repo = BatchMedicionRepositorio.getInstancia();
    }

    public ControladorBatchMedicion() {
        super("batch");
    }

    @Override
    protected void runBaseEndpoints() {
        super.runBaseEndpoints();

        post("/" + nombre, (req, res) -> {
            System.out.println(req.queryParams("org_id"));
            // JSONObject body = new JSONObject(req.body());
            OrganizacionRepositorio orgRepo = OrganizacionRepositorio.getInstancia();
            Organizacion org = orgRepo.get(Long.parseLong(req.queryParams("org_id")));
            Sector sect = null;
            if(req.queryParams("sect_id") != null){
                sect = SectorRepositorio.getInstancia().get(Long.parseLong(req.queryParams("sect_id")));
            }

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
                // Use the input stream to create a file
                File file = new File("tempFile.csv");
                copyInputStreamToFile(is, file);
                ParserCSV parser = new ParserCSV("tempFile.csv");

                List<Medicion> mediciones = (List<Medicion>) (List<?>) parser.leerMediciones();
                List<Medicion> medicionesPersistidas = new ArrayList<>();

                for (Medicion med : mediciones) {
                    med.setOrganizacion(org);
                    //aca persistir en 'cadena' atributos de la medicion;
                    Actividad actPers = med.getActividad();
                    //hack para que funcione, sino le estamos dejando un sector cualqueira
                    actPers.setSector(null);
                    if (req.queryParams("sect_id") != null) actPers.setSector(sect);
                    actPers = ActividadRepositorio.getInstancia().save(actPers);
                    System.out.println(actPers.getId());
                    med.setActividad(actPers);

                    Consumo consumo = med.getConsumo();

                    TipoConsumo tipoConsumo = consumo.getTipoConsumo();
                    
                    Unidad unidad = tipoConsumo.getUnidad();
                    FactorEmision factEmision = tipoConsumo.getFactorEmision();
                    Unidad unidadFE = factEmision.getUnidad();

                    unidad = UnidadRepositorio.getInstancia().save(unidad);
                    System.out.println(unidad.getId());
                    tipoConsumo.setUnidad(unidad);

                    unidadFE = UnidadRepositorio.getInstancia().save(unidadFE);
                    System.out.println(unidadFE.getId());
                    factEmision.setUnidad(unidadFE);

                    factEmision = FactorEmisionRepositorio.getInstancia().save(factEmision);
                    System.out.println(factEmision.getId());
                    tipoConsumo.setFactorEmision(factEmision);

                    tipoConsumo = TipoConsumoRepositorio.getInstancia().save(tipoConsumo);
                    System.out.println(tipoConsumo.getId());
                    consumo.setTipoConsumo(tipoConsumo);

                    consumo = ConsumoRepositorio.getInstancia().save(consumo);
                    System.out.println(consumo.getId());
                    med.setConsumo(consumo);

                    PeriodoDeImputacion periodo = PeriodoDeImputacionRepositorio.getInstancia().save(med.getPeriodoImputacion());
                    med.setPeriodoImputacion(periodo);


                    med = (Medicion) MedibleRepositorio.getInstancia().save(med);
                    System.out.println(med);
                    System.out.println("esta es la medicion que guardamos!!");
                    medicionesPersistidas.add(med);
                    System.out.println(med.id);
                    // org.addMedible(med);
                }
                BatchMedicion bm = new BatchMedicion(medicionesPersistidas);
                bm = repo.save(bm);
                System.out.println(org);
                orgRepo.save(org);

                res.status(202);
                return bm;
            }}, json());
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }

}
