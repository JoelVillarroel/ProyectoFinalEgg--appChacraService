package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author hdsot
 */
@Controller
@RequestMapping("/administrador")
public class AdministradorControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private PortalControlador portalControlador;
    
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo,HttpSession session){
        String redireccion = portalControlador.logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        List<Usuario>usuarios=usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios",usuarios);
        return "usuario_lista.html";
    }
    @GetMapping("/modificarEstado/{id}")
    public String modificarEstado(@PathVariable String id){
        usuarioServicio.cambiarEstado(id);
        
       return "redirect:/administrador/usuarios";
    }
    
}
