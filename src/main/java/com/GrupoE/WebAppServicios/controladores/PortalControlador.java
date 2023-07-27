package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Imagen;
import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.entidades.Trabajo;
import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.servicios.ProveedorServicio;
import com.GrupoE.WebAppServicios.servicios.TrabajoServicio;
import com.GrupoE.WebAppServicios.servicios.UsuarioServicio;
import java.util.List;
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

    /*-----------------------------------------------------------*/
    @GetMapping("/registrar")
    public String registrar() {
        return "registroUsuario.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String barrio, @RequestParam String direccion, @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, ModelMap modelo, MultipartFile archivo) throws MyException {
        try {
            usuarioServicio.registrar(archivo, nombre, apellido, barrio, direccion, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente");
            return "index.html";
        } catch (MyException ex) {
            modelo.put("Error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }
    }

    /*-----------------------------------------------------------*/
    @GetMapping("/registroProveedor")
    public String registroProveedor() {
        return "registroProveedor.html";
    }

    @PostMapping("/registroProveedor")
    public String registroProveedor(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String descripcion,
            @RequestParam String servicio,@RequestParam String remuneracion, @RequestParam String direccion, @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, ModelMap modelo, MultipartFile archivo) throws MyException {
        try {

            proveedorServicio.registrar(archivo, nombre, apellido, direccion, servicio, remuneracion,  descripcion, email, password, password2);

            
            

            modelo.put("exito", "Proveedor registrado correctamente");
            return "index.html";
        } catch (MyException ex) {
            modelo.put("Error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }
    }

    /*-----------------------------------------------------------*/
    @GetMapping("/conocenos")
    public String nosotros() {
        return "nosotros.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña invalidos!!");
        }

        return "login.html";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
        // Obtener el objeto Usuario de la sesión
        Usuario logueadoUsuario = (Usuario) session.getAttribute("usuarioSession");

        // Obtener el objeto Proveedor de la sesión
        Proveedor logueadoProveedor = (Proveedor) session.getAttribute("proveedorSession");

        if (logueadoUsuario != null) {
            if (logueadoUsuario.getRol().toString().equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            }
            modelo.addAttribute("nombre", logueadoUsuario.getNombre());
            modelo.addAttribute("apellido", logueadoUsuario.getApellido());
            modelo.addAttribute("barrio", logueadoUsuario.getBarrio());
            modelo.addAttribute("direccion", logueadoUsuario.getDireccion());
            modelo.addAttribute("email", logueadoUsuario.getEmail());
            modelo.addAttribute("rol", logueadoUsuario.getRol().toString());
        } else if (logueadoProveedor != null) {
            modelo.addAttribute("nombre", logueadoProveedor.getNombre());
            modelo.addAttribute("apellido", logueadoProveedor.getApellido());
            modelo.addAttribute("direccion", logueadoProveedor.getDireccion());
            modelo.addAttribute("descripcion", logueadoProveedor.getDescripcion());
            modelo.addAttribute("imagen", logueadoProveedor.getImagen());
            modelo.addAttribute("email", logueadoProveedor.getEmail());
            modelo.addAttribute("rol", logueadoProveedor.getRol().toString());
        } else {
            // Manejar el caso cuando no hay usuario ni proveedor logueado
            // Puedes redirigir a una página de inicio de sesión o manejarlo de otra manera
            return "redirect:/login";
        }

        return "inicio.html";
    }
}
