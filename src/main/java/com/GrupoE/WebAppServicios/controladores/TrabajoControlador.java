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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// @author Camila Astrada 
@Controller
@RequestMapping("/trabajo")
public class TrabajoControlador {

    @Autowired

    private TrabajoServicio trabajoServicio;

    @Autowired
    private ProveedorServicio proveedorServicio;

    @GetMapping("/registrarTrabajo/{id}")
    public String registrarTrabajo(@PathVariable String id, HttpSession session, ModelMap modelo, ModelMap modeloUsuario) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modeloUsuario.put("logueado", logueado);
        modelo.put("proveedor", proveedorServicio.getOne(id));

        return "registroTrabajo.html";
    }

    /*
    @GetMapping("vista/{id}")
    public String vista(@PathVariable String id, ModelMap modelo){
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "vista_noticia.html";
    }
     */
    @PostMapping("/registroTrabajo")

    public String registroTrabajo(@RequestParam String idLogueado, @RequestParam String idProveedor, @RequestParam String descripcion,
            ModelMap modelo) throws MyException {
        try {
            trabajoServicio.registrar(idLogueado, idProveedor, descripcion);
            modelo.put("exito", "Proveedor registrado correctamente");
            return "index.html";
        } catch (MyException ex) {
            modelo.put("Error", ex.getMessage());

            return "registroTrabajo.html";
        }
    }

    @GetMapping("/listaTrabajo")
    public String listarAllTrabajos(ModelMap modelo, String idProveedor, HttpSession session) {

        List<Trabajo> trabajos = trabajoServicio.listarTrabajos();  //lista todos los trabajos
        modelo.addAttribute("trabajos", trabajos);

       //lista los trabajos no realizados de un proveedor en particular
        List<Trabajo> NoRealizados = trabajoServicio.listarTrabajosNoRealizados(idProveedor);
        modelo.addAttribute("NoRealizados", NoRealizados);

        //lista los trabajos que le faltan calificar al ususario logueado.
        Usuario UsuarioLogueado = (Usuario) session.getAttribute("usuarioSession");
        List<Trabajo> RealizadosNoCalificados = trabajoServicio.trabajosRealizadosNoCalificados(UsuarioLogueado.getId());
        modelo.addAttribute("RealizadosNoCalificados", RealizadosNoCalificados);

        //lista todos los trabajos de un proveedor en particular
        List<Trabajo> trabajosProov = trabajoServicio.TodosProveedor(idProveedor);
        modelo.addAttribute("trabajosProov", trabajosProov);

        //lista los trabajos que no ha aceptado ni rechazado de un proveedor en particular
        List<Trabajo> solicitudes = trabajoServicio.Solicitudes(idProveedor);
        modelo.addAttribute("solicitudes", solicitudes);

        //lista los trabajos realizados de un proveedor en particular para mostrarle al usuario
        List<Trabajo> RealizadosProveedor = trabajoServicio.RealizadosProveedor(idProveedor);
        modelo.addAttribute("RealizadosProveedor", RealizadosProveedor);

        return "trabajo_lista.html";

    }

    /*  
        @GetMapping("/listaTrabajo")
    public String listarTrabajosDeUnProveedor(ModelMap modelo,String idProveedor){
        
        List<Trabajo> trabajosProov= trabajoServicio.listarTrabajosPorIdProveedor(idProveedor)
        modelo.addAttribute("trabajosProov",trabajosProov);
        return "trabajo_lista.html";
    }
   
@GetMapping("/listaTrabajo")
    public String listarAllTrabajos(ModelMap modelo){
        
        List<Trabajo> trabajos= trabajoServicio.listarTrabajos();
        return "trabajo_lista.html";
    }
    
   @GetMapping("/listaTrabajo")
    public String listarTrabajosNoRealizados(ModelMap modelo,String idProveedor){
        
        List<Trabajo> NoRealizados= trabajoServicio.listarTrabajosNoRealizados(idProveedor);
        modelo.addAttribute("NoRealizados",NoRealizados);
        return "trabajo_lista.html";
    }
    
        @GetMapping("/listaTrabajo")
    public String trabajosRealizadosNoCalificados(ModelMap modelo,String idProveedor, HttpSession session){
         Usuario UsuarioLogueado = (Usuario) session.getAttribute("usuarioSession");
        List<Trabajo> NoRealizados= trabajoServicio.trabajosRealizadosNoCalificados(idProveedor, UsuarioLogueado.getId());
        modelo.addAttribute("RealizadosNoCalificados",RealizadosNoCalificados);
        return "trabajo_lista.html";
    }*/
}
