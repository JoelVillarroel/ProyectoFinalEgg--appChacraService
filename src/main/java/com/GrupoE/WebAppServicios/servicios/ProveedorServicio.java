package com.GrupoE.WebAppServicios.servicios;

import com.GrupoE.WebAppServicios.entidades.Imagen;
import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.entidades.Trabajo;
import com.GrupoE.WebAppServicios.enumeraciones.Rol;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.repositorios.ProveedorRepositorio;
import com.GrupoE.WebAppServicios.repositorios.TrabajoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProveedorServicio implements UserDetailsService {

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private TrabajoRepositorio trabajoRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional

    public void registrar(MultipartFile archivo, String nombre, String apellido, String direccion, String servicio, String remuneracion, String descripcion, String email, String password, String password2) throws MyException {


        validar(nombre, apellido, direccion, descripcion,remuneracion, email, password, password2);

        Proveedor proveedor = new Proveedor();
        proveedor.setCantTrabajos(0);
        proveedor.setCalificacion(0);
        proveedor.setNombre(nombre);

        proveedor.setApellido(apellido);
        proveedor.setDireccion(direccion);
        proveedor.setServicio(servicio);

        proveedor.setDescripcion(descripcion);
        proveedor.setRemuneracion(remuneracion);

        proveedor.setEmail(email);

        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));

        proveedor.setRol(Rol.PROVEEDOR);

        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setImagen(imagen);

        proveedorRepositorio.save(proveedor);
    }

    @Transactional
    public void actualizar(MultipartFile archivo, String idProveedor, String idTrabajo, Integer cantTrabajos, String nombre, String apellido, String direccion, String descripcion,String remuneracion, String email, String password, String password2) throws MyException {
        Integer calificacion = 0;
        validar(nombre, apellido, direccion, descripcion, remuneracion, email, password, password2);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(idTrabajo);
        if (respuestaTrabajo.isPresent()) {
            Trabajo trabajo = respuestaTrabajo.get();
            calificacion = trabajo.getCalificacion();
            cantTrabajos = cantTrabajos + 1;
        }
        if (respuesta.isPresent()) {

            Proveedor proveedor = respuesta.get();
            proveedor.setCalificacion((calificacion + proveedor.getCalificacion()) / cantTrabajos);
            proveedor.setNombre(nombre);
            proveedor.setEmail(email);
            proveedor.setApellido(apellido);
            proveedor.setDireccion(direccion);

            proveedor.setPassword(new BCryptPasswordEncoder().encode(password));

            proveedor.setRol(Rol.PROVEEDOR);

            String idImagen = null;

            if (proveedor.getImagen() != null) { //si hay imagen para cargar , la cargamos
                idImagen = proveedor.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            proveedor.setImagen(imagen);

            proveedorRepositorio.save(proveedor);
        }
    }

    public Proveedor getOne(String id) {
        return proveedorRepositorio.getOne(id);
    }
    
    @Transactional//(readOnly=True)
    public List<Proveedor> listarProveedores() {

        List<Proveedor> proveedores = new ArrayList();

        proveedores = proveedorRepositorio.findAll();

        return proveedores;
    }

    @Transactional//(readOnly=True)
    public List<Proveedor> listarProveedoresPorDescripcion(String servicio) {

        List<Proveedor> proveedores = new ArrayList();

        proveedores = proveedorRepositorio.buscarPorNombreDescripcion(servicio);

        return proveedores;
    }

    @Transactional
    public void eliminar(String id) throws MyException {
        Proveedor proveedor = proveedorRepositorio.getById(id);
        proveedorRepositorio.delete(proveedor);
    }

    private void validar(String nombre, String apellido, String direccion, String descripcion,String remuneracion, String email, String password, String password2) throws MyException {

        /*Validar Nombre*/
        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("El nombre no pude ser nulo ni estar vacio");
        }
        String nombrevalidar = nombre.toUpperCase();
        for (int i = 0; i < nombrevalidar.length(); i++) {
            char letra = nombrevalidar.charAt(i);
            if (letra == 32) {
                continue;
            }
            if ((letra < 65 || letra > 90) && (letra != 209)) {
                throw new MyException("El nombre contiene algo que no sea una letra");
            }
        }
        /*Validar Apellido*/
        if (apellido == null || apellido.isEmpty()) {
            throw new MyException("El apellido no puede ser nulo o estar vacío");
        }
        String apellidovalidar = apellido.toUpperCase();
        for (int i = 0; i < apellidovalidar.length(); i++) {
            char letra = apellidovalidar.charAt(i);
            if (letra == 32) {
                continue;
            }
            if ((letra < 65 || letra > 90) && (letra != 209)) {
                throw new MyException("El apellido contiene algo que no sea una letra");
            }
        }
        /*Validar Descripcion*/
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MyException("La descripcion no puede ser nula o estar vacío");
        }
        /*Validar Remuneracion*/
        if (remuneracion == null || remuneracion.isEmpty()) {
            throw new MyException("La remuneracion no puede ser nula o estar vacío");
        }
        boolean val=false;
        try {
            double valor = Double.parseDouble(remuneracion);
        } catch (Exception e) {
            val=true;
        }
        if (val) {
            throw new MyException("La remuneracion contiene algo que no sea un numero");
        }
        /*Validar Direccion*/
        if (direccion == null || direccion.isEmpty()) {
            throw new MyException("La direccion no puede ser nula o estar vacío");
        }
        /*Validar Email*/
        if (email == null || email.isEmpty()) {
            throw new MyException("El email no puede ser nulo o estar vacío");
        }
        /*Validar contraseña*/
        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MyException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas ingresadas deben ser iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Proveedor proveedor = proveedorRepositorio.buscarProveedorPorEmail(email);
        if (proveedor != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + proveedor.getRol().toString());//concatenacion ROLE_USER

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession sessionP = attr.getRequest().getSession(true);

            sessionP.setAttribute("proveedorSession", proveedor);

            ServletRequestAttributes attrP = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attrP.getRequest().getSession(true);
            session.setAttribute("proveedorsession", proveedor);


            return new User(proveedor.getEmail(), proveedor.getPassword(), permisos);
        } else {
            return null;
        }
    }

}
