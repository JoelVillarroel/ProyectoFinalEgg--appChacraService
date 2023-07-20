/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.GrupoE.WebAppServicios.servicios;

import com.GrupoE.WebAppServicios.entidades.Imagen;
import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.enumeraciones.Rol;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.repositorios.ProveedorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author hdsot
 */
@Service
public class ProveedorServicio {
    
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String descripcion, String email, String password, String password2) throws MyException {

        validar(nombre, apellido, descripcion, email, password, password2);

        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(nombre);

        proveedor.setApellido(apellido);

        proveedor.setDescripcion(descripcion);

        proveedor.setEmail(email);

        proveedor.setPassword(password);

        proveedor.setRol(Rol.USER);

        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setImagen(imagen);

        proveedorRepositorio.save(proveedor);
    }

    @Transactional
    public void actualizar(MultipartFile archivo, String idProveedor, String nombre, String email, String password, String password2) throws MyException {

        validar(nombre, email, email, email, password, password2);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {

            Proveedor proveedor = respuesta.get();
            proveedor.setNombre(nombre);
            proveedor.setEmail(email);

            proveedor.setPassword(password);

            proveedor.setRol(Rol.USER);

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
    
    @Transactional
    public void eliminar(String id) throws MyException{
        Proveedor proveedor=proveedorRepositorio.getById(id);
        proveedorRepositorio.delete(proveedor);
    }

    private void validar(String nombre, String apellido, String descripcion, String email, String password, String password2) throws MyException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("El nombre no pude ser nulo ni estar vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MyException("El apellido no puede ser nulo o estar vacío");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MyException("La descripcion no puede ser nula o estar vacío");
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
}
