/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.GrupoE.WebAppServicios.servicios;

import com.GrupoE.WebAppServicios.entidades.Imagen;
import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.enumeraciones.Rol;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.repositorios.ProveedorRepositorio;
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
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String direccion,String descripcion, String email, String password, String password2) throws MyException {

        validar(nombre, apellido, direccion, descripcion, email, password, password2);

        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(nombre);

        proveedor.setApellido(apellido);
        proveedor.setDireccion(direccion);

        proveedor.setDescripcion(descripcion);

        proveedor.setEmail(email);

        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));

        proveedor.setRol(Rol.PROVEEDOR);

        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setImagen(imagen);

        proveedorRepositorio.save(proveedor);
    }

    @Transactional
    public void actualizar(MultipartFile archivo, String idProveedor, String nombre,String apellido,String direccion,String descripcion, String email, String password, String password2) throws MyException {

        validar(nombre, apellido, direccion, descripcion, email, password, password2);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {

            Proveedor proveedor = respuesta.get();
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
    public List<Proveedor> listarProveedoresPorDescripcion(String descripcion) {

        List<Proveedor> proveedores = new ArrayList();

       proveedores = proveedorRepositorio.buscarPorNombreDescripcion(descripcion);

        return proveedores;
    }
    
    @Transactional
    public void eliminar(String id) throws MyException{
        Proveedor proveedor=proveedorRepositorio.getById(id);
        proveedorRepositorio.delete(proveedor);
    }

    private void validar(String nombre, String apellido,String direccion, String descripcion, String email, String password, String password2) throws MyException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("El nombre no pude ser nulo ni estar vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MyException("El apellido no puede ser nulo o estar vacío");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MyException("La descripcion no puede ser nula o estar vacío");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new MyException("La direccion no puede ser nula o estar vacío");
        }
        if (email == null || email.isEmpty()) {
            throw new MyException("El email no puede ser nulo o estar vacío");
        }
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
         if(proveedor!=null){
            List<GrantedAuthority> permisos= new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+proveedor.getRol().toString());//concatenacion ROLE_USER
             
            permisos.add(p);
             
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
            
            session.setAttribute("proveedorSession", proveedor);
             
             return new User(proveedor.getEmail(),proveedor.getPassword(),permisos);
         }else{
             return null;
         }
    }
}
