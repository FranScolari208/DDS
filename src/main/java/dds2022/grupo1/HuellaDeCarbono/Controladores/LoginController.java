package dds2022.grupo1.HuellaDeCarbono.Controladores;

import dds2022.grupo1.HuellaDeCarbono.Repositorios.MiembroRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.OrganizacionRepositorio;
import dds2022.grupo1.HuellaDeCarbono.Repositorios.UsuarioRepositorio;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Organizacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginController {
    private static final UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstancia();
    private static final OrganizacionRepositorio organizacionRepositorio = OrganizacionRepositorio.getInstancia();

    private static final MiembroRepositorio miembroRepositorio = MiembroRepositorio.getInstancia();
   //public static final String USUARIO_MIEMBRO = "miembro";


    public Boolean estaAutenticado(Request request) {
        Integer usuarioId = request.session().attribute("id");
        if (usuarioId != null) {
            return usuarioRepositorio.get(usuarioId) != null;
        } else {
            return false;
        }
    }

    public ModelAndView login(Request request, Response response) {
        if (estaAutenticado(request)) {
           // response.redirect("/ListadoOrganizaciones");
            return new ModelAndView(null, "ListadoOrganizaciones.hbs");
        } else {
            return new ModelAndView(null, "login.hbs");
        }
    }

    public ModelAndView loginError(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("error", true);
        return new ModelAndView(parametros, "login.hbs");
    }

    public Response iniciarSesion(Request request, Response response) {
        // try {
       // HashMap<String, Object> parametros = new HashMap<>();
        String usuarioNombre = request.queryParams("emailUser");
        String usuarioPassword = request.queryParams("passwordUser");

        // if (usuarioRepositorio.existe(usuarioNombre)) {
        // Usuario usuario = usuarioRepositorio.buscarUsuario(usuarioNombre);

        Usuario usuario = usuarioRepositorio.findUsuario(usuarioNombre);
        // BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        if (usuarioPassword.equals(usuarioRepositorio.findUsuarioContrasenia(usuarioNombre))) {
            request.session(true);
            request.session().attribute("id", usuario.getId());
            request.session().attribute("email", usuario.getEmail());
            request.session().attribute("miembro", usuario.getMiembro());

        } else {
            response.redirect("/loginError");
        }
        //} else {
        //   response.redirect("/loginError");
        //}

        //} catch (Exception e) {
        //   e.printStackTrace();
        // response.redirect("/loginError");
        //} finally {
        //return response;
       /* OrganizacionRepositorio organizacionRepositorio = OrganizacionRepositorio.getInstancia();
        List<Organizacion> organizaciones=usuarioRepositorio.findOrganizaciones(usuarioNombre);
        parametros.put("organizaciones", organizaciones);
        return new ModelAndView(parametros, "ListadoOrganizaciones.hbs");*/

        response.redirect("/home1");
        return response;
    }
        //}


    public ModelAndView mostrarOrganizaciones(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        String email = request.session().attribute("email");
        List<Organizacion> organizaciones=usuarioRepositorio.findOrganizaciones(email);
        parametros.put("organizaciones", organizaciones);
        return new ModelAndView(parametros, "ListadoOrganizaciones.hbs");

        }

    public ModelAndView mostrarOrganizacionesNoRegistrado(Request request, Response response) {
        HashMap<String, Object> parametros = new HashMap<>();
        List<Organizacion> organizaciones = organizacionRepositorio.getAllOrganizaciones();
        parametros.put("organizaciones", organizaciones);
        return new ModelAndView(parametros, "organizacionesNoRegistrado.html");

    }

}



