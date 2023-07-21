package com.GrupoE.WebAppServicios.servicios;

import com.GrupoE.WebAppServicios.entidades.Imagen;
import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.enumeraciones.Rol;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService{

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String direccion, String email, String password, String password2) throws MyException {

        validar(nombre, apellido, direccion, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);

        usuario.setApellido(apellido);

        usuario.setDireccion(direccion);

        usuario.setEmail(email);

        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

        usuario.setRol(Rol.USER);

        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String password2) throws MyException {

        validar(nombre, email, email, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);

            usuario.setPassword(password);

            usuario.setRol(Rol.USER);

            String idImagen = null;

            if (usuario.getImagen() != null) { //si hay imagen para cargar , la cargamos
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional//(readOnly=True)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }
    
    @Transactional
    public void eliminar(String id) throws MyException{
        Usuario usuario=usuarioRepositorio.getById(id);
        usuarioRepositorio.delete(usuario);
    }
    
    @Transactional
    public List<Usuario> buscarPorApelido(String apellido) throws MyException{
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.buscarPorApellidoUsuarios(apellido);
        return usuarios;
    }

    private void validar(String nombre, String apellido, String direccion, String email, String password, String password2) throws MyException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("El nombre no pude ser nulo ni estar vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MyException("El apellido no puede ser nulo o estar vacío");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new MyException("La direccion no puede ser nulo o estar vacío");
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
        Usuario usuario = usuarioRepositorio.buscarUsuarioPorEmail(email);
         if(usuario!=null){
             List<GrantedAuthority> permisos= new ArrayList();
             GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+usuario.getRol().toString());//concatenacion ROLE_USER
             
             permisos.add(p);
             
             return new User(usuario.getEmail(),usuario.getPassword(),permisos);
         }else{
             return null;
         }
    }
}
