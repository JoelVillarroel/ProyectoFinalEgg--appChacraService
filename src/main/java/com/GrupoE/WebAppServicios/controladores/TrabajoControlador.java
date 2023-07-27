

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
    public String registrarTrabajo(@PathVariable String id,HttpSession session ,ModelMap modelo,ModelMap modeloUsuario) {
        Usuario logueado=(Usuario) session.getAttribute("usuariosession");
        modeloUsuario.put("logueado",logueado);
        modelo.put("proveedor",proveedorServicio.getOne(id));
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
    
    /*
    public String registroTrabajo(@RequestParam String descripcion, ModelMap modelo) throws MyException {
        try {
            trabajoServicio.registrar("2", "2", descripcion);
            modelo.put("exito", "Trabajo registrado correctamente");
            return "inicio.html";
        } catch (MyException ex) {
            modelo.put("Error", ex.getMessage());
            modelo.put("descripcion", descripcion);
            return "registroTrabajo.html";
        }
    }*/

    /*-----------------------------------------------------------*/
      @GetMapping("/listaTrabajo")
    public String listarAllTrabajos(ModelMap modelo){
        
        List<Trabajo> trabajos= trabajoServicio.listarTrabajos();
        modelo.addAttribute("trabajos",trabajos);
        return "trabajo_lista.html";
    }
    
    
}
