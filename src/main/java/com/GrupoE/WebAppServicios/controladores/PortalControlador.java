package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.servicios.ProveedorServicio;
import com.GrupoE.WebAppServicios.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String registrar(ModelMap modelo, HttpSession session) {
        String redireccion = logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        return "registroUsuario.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String barrio, @RequestParam String direccion, @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, ModelMap modelo, MultipartFile archivo,HttpSession session) throws MyException {
        String redireccion = logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        try {
            usuarioServicio.registrar(archivo, nombre, apellido, barrio, direccion, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente");
            return "index.html";
        } catch (MyException ex) {
            modelo.put("Error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registroUsuario.html";
        }
    }

    /*-----------------------------------------------------------*/
    @GetMapping("/registroProveedor")
    public String registroProveedor() {
        return "registroProveedor.html";
    }

    @PostMapping("/registroProveedor")
    public String registroProveedor(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String descripcion,
            @RequestParam String servicio, @RequestParam String remuneracion, @RequestParam String direccion, @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, ModelMap modelo, MultipartFile archivo, HttpSession session) throws MyException {
        String redireccion = logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        try {

            proveedorServicio.registrar(archivo, nombre, apellido, direccion, servicio, remuneracion, descripcion, email, password, password2);

            modelo.put("exito", "Proveedor registrado correctamente");
            return "index.html";
        } catch (MyException ex) {
            modelo.put("Error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registroProveedor.html";
        }
    }

    /*-----------------------------------------------------------*/
    @GetMapping("/conocenos")
    public String nosotros(ModelMap modelo, HttpSession session) {
        String redireccion = logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        return "nosotros.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo,HttpSession session) {
        String redireccion = logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }
        if (error != null) {
            modelo.put("error", "Usuario o contraseña invalidos!!");
        }

        return "login.html";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
        // Llamamos al método logueado del controlador base para cargar los datos del usuario o proveedor logueado
        String redireccion = logueado(modelo, session);
        if (redireccion != null) {
            // Si el método logueado devuelve una redirección, la retornamos
            return redireccion;
        }

        return "inicio.html";
    }

    protected String logueado(ModelMap modelo, HttpSession session) {
        // Tu código para cargar los datos del usuario o proveedor logueado
        // Si es un usuario logueado
        Usuario logueadoUsuario = (Usuario) session.getAttribute("usuarioSession");
        if (logueadoUsuario != null) {
            if (logueadoUsuario.getRol().toString().equals("ADMIN")) {
                // No redirigimos aquí, simplemente retornamos null
                modelo.addAttribute("nombre", logueadoUsuario.getNombre());
                modelo.addAttribute("apellido", logueadoUsuario.getApellido());
                modelo.addAttribute("barrio", logueadoUsuario.getBarrio());
                modelo.addAttribute("direccion", logueadoUsuario.getDireccion());
                modelo.addAttribute("email", logueadoUsuario.getEmail());
                modelo.addAttribute("rol", logueadoUsuario.getRol().toString());
                return null;
            }
            modelo.addAttribute("nombre", logueadoUsuario.getNombre());
            modelo.addAttribute("apellido", logueadoUsuario.getApellido());
            modelo.addAttribute("barrio", logueadoUsuario.getBarrio());
            modelo.addAttribute("direccion", logueadoUsuario.getDireccion());
            modelo.addAttribute("email", logueadoUsuario.getEmail());
            modelo.addAttribute("rol", logueadoUsuario.getRol().toString());
        } // Si es un proveedor logueado
        else {
            Proveedor logueadoProveedor = (Proveedor) session.getAttribute("proveedorSession");
            if (logueadoProveedor != null) {
                modelo.addAttribute("nombre", logueadoProveedor.getNombre());
                modelo.addAttribute("apellido", logueadoProveedor.getApellido());
                modelo.addAttribute("direccion", logueadoProveedor.getDireccion());
                modelo.addAttribute("descripcion", logueadoProveedor.getDescripcion());
                modelo.addAttribute("imagen", logueadoProveedor.getImagen().getId());
                modelo.addAttribute("email", logueadoProveedor.getEmail());
                modelo.addAttribute("rol", logueadoProveedor.getRol().toString());
            } // Si no hay usuario ni proveedor logueado
        }

        return null; // Retornamos null si todo va bien (sin redirecciones)
    }
}
