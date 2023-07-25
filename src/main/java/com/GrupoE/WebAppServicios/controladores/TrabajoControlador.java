

package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Trabajo;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.servicios.TrabajoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


 // @author Camila Astrada 
@Controller
@RequestMapping("/trabajo")
public class TrabajoControlador {

    
     @Autowired
    private TrabajoServicio trabajoServicio;
    @GetMapping("/registrarTrabajo")
    public String registroTrabajo() {
        return "registroTrabajo.html";
    }

    @PostMapping("/registroTrabajo")
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
    }

    /*-----------------------------------------------------------*/
      @GetMapping("/listaTrabajo")
    public String listarAllTrabajos(ModelMap modelo){
        
        List<Trabajo> trabajos= trabajoServicio.listarTrabajos();
        modelo.addAttribute("trabajos",trabajos);
        return "trabajo_lista.html";
    }
    
    
}