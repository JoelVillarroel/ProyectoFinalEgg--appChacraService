package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.servicios.ProveedorServicio;
import com.GrupoE.WebAppServicios.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;
    
    @GetMapping("/")
    public String index() {
        return "Index.html";
    }
    
    @GetMapping("/registrar")
    public String registrar(){
        return "registroUsuario.html";
    }
    @PostMapping("/registro")
    public String registro (@RequestParam String nombre,@RequestParam String apellido,@RequestParam String direccion,@RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, ModelMap modelo,MultipartFile archivo)throws MyException{
        try {
            usuarioServicio.registrar(archivo, nombre,apellido,direccion, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente");
            return "index.html";
        } catch (MyException ex) {
            modelo.put("Error",ex.getMessage());
            modelo.put("nombre",nombre);
            modelo.put("email",email);
            return "registro.html";
        }
    }
    @PostMapping("/registroProveedor")
    public String registroProveedor (@RequestParam String nombre,@RequestParam String apellido,@RequestParam String direccion,@RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, ModelMap modelo,MultipartFile archivo)throws MyException{
        try {
            proveedorServicio.registrar(archivo, nombre,apellido,direccion, email, password, password2);
            modelo.put("exito", "Proveedor registrado correctamente");
            return "index.html";
        } catch (MyException ex) {
            modelo.put("Error",ex.getMessage());
            modelo.put("nombre",nombre);
            modelo.put("email",email);
            return "registro.html";
        }
    }
    @GetMapping("/conocenos")
    public String nosotros(){
        return "nosotros.html";
    }
    @GetMapping("/registroProveedor")
    public String registroProveedor(){
        return "registroProveedor.html";
    }
    @GetMapping("/login")
    public String login(@RequestParam(required=false)String error,ModelMap modelo){
        if(error!=null){
            modelo.put("error", "Usuario o contraseña invalidos!!");
        }
        
        return "login.html";
    }
    
    @GetMapping("/inicio")
    public String inicio(HttpSession session){
        
        Usuario logueado =(Usuario) session.getAttribute("usuariosession");
        
        if(logueado.getRol().toString().equals("ADMIN")){
            return "redirect:/admin/dashboard";
        }
        
        return "Index.html";
    }
}
