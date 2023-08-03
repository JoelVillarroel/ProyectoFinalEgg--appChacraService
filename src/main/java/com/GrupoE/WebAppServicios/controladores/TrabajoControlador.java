package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.entidades.Trabajo;
import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.servicios.ProveedorServicio;
import com.GrupoE.WebAppServicios.servicios.TrabajoServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// @author Camila Astrada 
@Controller
@RequestMapping("/trabajo")
public class TrabajoControlador {

    @Autowired
    private TrabajoServicio trabajoServicio;
    @Autowired
    private PortalControlador portalControlador;
    @Autowired
    private ProveedorServicio proveedorServicio;
    
    @GetMapping("/registrarTrabajo/{id}")
    public String registrarTrabajo(@PathVariable String id, HttpSession session, ModelMap modelo, ModelMap modeloUsuario) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioSession");
        // Cargamos los datos del usuario o proveedor logueado directamente aquí
        String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        modeloUsuario.put("logueado", logueado);
        modelo.put("proveedor", proveedorServicio.getOne(id));
        List<Trabajo> trabajosTodos=trabajoServicio.TodosProveedor(id);
        modelo.addAttribute("trabajosTodos", trabajosTodos);
        return "proveedor.html";
    }
    
/*
    @GetMapping("/registrarTrabajo")
    public String registroTrabajo(@RequestParam(name = "id", required = false, defaultValue = "0") String id,
            ModelMap modelo, HttpSession session, ModelMap modeloUsuario) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modeloUsuario.put("logueado", logueado);
        modelo.put("proveedor", proveedorServicio.getOne(id));
        String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }

        return "registroTrabajo.html";
    }
*/
    /*
    @GetMapping("vista/{id}")
    public String vista(@PathVariable String id, ModelMap modelo){
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "vista_noticia.html";
    }
     */
    @PostMapping("/registroTrabajo")
    public String registroTrabajo(@RequestParam String idLogueado, @RequestParam String idProveedor, @RequestParam(required = false, defaultValue = " Sin descripcion") String descripcion,
             ModelMap modelo) throws MyException {
        
        try {
            trabajoServicio.registrar(idLogueado, idProveedor, descripcion);
            modelo.put("exito", "Trabajo registrado correctamente");

            return "inicio.html";
        } catch (MyException ex) {
            modelo.put("Error", ex.getMessage());
            return "registroTrabajo.html";
        }
    }

    @GetMapping("/listaTrabajo/allTrabajos")

    public String listarAllTrabajos(ModelMap modelo, String idProveedor, HttpSession session) {
        String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        List<Trabajo> trabajos = trabajoServicio.listarTrabajos();  //lista todos los trabajos
        modelo.addAttribute("trabajos", trabajos);

        //lista los trabajos no realizados de un proveedor en particular


        return "trabajo_lista.html";

    }
    
    @GetMapping("/listaTrabajo/usuario")
    public String listarTrabajosUsuario(ModelMap modelo, String idProveedor,  HttpSession session) {
        
          idProveedor = "05f79207-7383-4799-ad68-c3f92a424d0c";
        //ESto ven los usuarios
         //lista los trabajos que le faltan calificar al ususario logueado.
        Usuario UsuarioLogueado = (Usuario) session.getAttribute("usuarioSession");
        List<Trabajo> RealizadosNoCalificados = trabajoServicio.trabajosRealizadosNoCalificados(UsuarioLogueado.getId());
        modelo.addAttribute("RealizadosNoCalificados", RealizadosNoCalificados);

         List<Trabajo> TodosUsuario = trabajoServicio.TodosUsuario(UsuarioLogueado.getId());
        modelo.addAttribute("TodosUsuario", TodosUsuario);
        

        //lista los trabajos realizados de un proveedor en particular para mostrarle al usuario
        List<Trabajo> RealizadosProveedor = trabajoServicio.RealizadosProveedor(idProveedor);
        modelo.addAttribute("RealizadosProveedor", RealizadosProveedor);
        
        return "trabajoproveedor_lista.html";
    }
    
    @GetMapping("/listaTrabajo/proveedor")
    public String listarTrabajosProveedor(ModelMap modelo, String idProveedor,  HttpSession session) {
        
        Proveedor logueadoProveedor = (Proveedor) session.getAttribute("proveedorSession");
        List<Trabajo> NoRealizados = trabajoServicio.listarTrabajosNoRealizados(logueadoProveedor.getId());
        modelo.addAttribute("NoRealizados", NoRealizados);
        
        //lista todos los trabajos de un proveedor en particular
        List<Trabajo> trabajosProov = trabajoServicio.TodosProveedor(logueadoProveedor.getId());
        modelo.addAttribute("trabajosProov", trabajosProov);

        //lista los trabajos que no ha aceptado ni rechazado de un proveedor en particular
        List<Trabajo> solicitudes = trabajoServicio.Solicitudes(logueadoProveedor.getId());
        modelo.addAttribute("solicitudes", solicitudes);
          
        return "trabajoproveedor_lista.html";
    }
  
    
//FUNCIONALIDADES
// RestringirComentario
    @GetMapping("/borrarComentario/{id}")
    public String ComentarioInapropiado(@PathVariable String id){
        trabajoServicio.ComentarioInapropiado(id);
        
         return "redirect:/trabajo/listaTrabajo/allTrabajos?cache=false";

    }
    
    @PostMapping("/aceptarSolicitud/{id}")
    public String Aceptar(@PathVariable String id) {
        trabajoServicio.AceptarSolicitud(id);
        
         return "redirect:/trabajo/listaTrabajo/proveedor?cache=false";


    }
    
     @PostMapping("/rechazarSolicitud/{id}")
    public String Rechazar(@PathVariable String id) {
        trabajoServicio.RechazarSolicitud(id);
        
         return "redirect:/trabajo/listaTrabajo/proveedor?cache=false";


    }
    
    @PostMapping("/marcarRealizado/{id}")
    public String MarcarRealizado(@PathVariable String id) throws MyException {
        trabajoServicio.MarcarComoRealizado(id);
        
         return "redirect:/trabajo/listaTrabajo/proveedor?cache=false";


    }
    
    @PostMapping("/cancelarSolicitud/{id}")
    public String CancelarSolicitud(@PathVariable String id) {
        trabajoServicio.CancelarSolicitud(id);
        
         return "redirect:/trabajo/listaTrabajo/usuario?cache=false";


    }
    
    
     @PostMapping("/calificar/{id}")
    public String Calificar(@PathVariable String id, @PathVariable String comentario, 
            @PathVariable int calificacion) throws MyException {
        trabajoServicio.Calificar(id, comentario, calificacion);
        
         return "redirect:/trabajo/listaTrabajo/usuario?cache=false";


    }
    /*  
    
    

      

    --------------
        @GetMapping("/listaTrabajo/trabajoDeUnProveedor")
    public String listarTrabajosDeUnProveedor(ModelMap modelo,String idProveedor, HttpSession session){
        String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        List<Trabajo> trabajosProov= trabajoServicio.listarTrabajosPorIdProveedor(idProveedor)
        modelo.addAttribute("trabajosProov",trabajosProov);
        return "trabajo_lista.html";
    }
   
@GetMapping("/listaTrabajo/todosLosTrabajos")
    public String listarAllTrabajos(ModelMap modelo, HttpSession session){
        String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        List<Trabajo> trabajos= trabajoServicio.listarTrabajos();
        return "trabajo_lista.html";
    }
    
   @GetMapping("/listaTrabajo/trabajosNoRealizados")
    public String listarTrabajosNoRealizados(ModelMap modelo,String idProveedor,HttpSession session){
        String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        List<Trabajo> NoRealizados= trabajoServicio.listarTrabajosNoRealizados(idProveedor);
        modelo.addAttribute("NoRealizados",NoRealizados);
        return "trabajo_lista.html";
    }
    
        @GetMapping("/listaTrabajo/trabajosRealizadosNoCalificados")
    public String trabajosRealizadosNoCalificados(ModelMap modelo,String idProveedor, HttpSession session){
String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
         Usuario UsuarioLogueado = (Usuario) session.getAttribute("usuarioSession");
        List<Trabajo> NoRealizados= trabajoServicio.trabajosRealizadosNoCalificados(idProveedor, UsuarioLogueado.getId());
        modelo.addAttribute("RealizadosNoCalificados",RealizadosNoCalificados);
        return "trabajo_lista.html";
    }*/
}
