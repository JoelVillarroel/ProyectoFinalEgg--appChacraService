package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo){
        List<Usuario>usuarios=usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios",usuarios);
        return "usuario_lista.html";
    }
    
}
