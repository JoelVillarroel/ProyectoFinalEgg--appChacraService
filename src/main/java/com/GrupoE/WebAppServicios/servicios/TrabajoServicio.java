package com.GrupoE.WebAppServicios.servicios;

import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.entidades.Trabajo;
import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.errores.MyException;
import com.GrupoE.WebAppServicios.repositorios.ProveedorRepositorio;
import com.GrupoE.WebAppServicios.repositorios.TrabajoRepositorio;
import com.GrupoE.WebAppServicios.repositorios.UsuarioRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrabajoServicio {

    @Autowired
    private TrabajoRepositorio trabajoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Transactional
    public void registrar(String idUsuario, String idProveedor, String descripcion) throws MyException {

        validar(idUsuario, idProveedor, descripcion);

        Optional<Usuario> user = usuarioRepositorio.findById(idUsuario);
        Trabajo trabajo = new Trabajo();

        if (user.isPresent()) {
            Usuario usuario = user.get();
            trabajo.setUsuario(usuario);
        }

        Optional<Proveedor> prov = proveedorRepositorio.findById(idProveedor);

        if (prov.isPresent()) {
            Proveedor proveedor = prov.get();
            trabajo.setProveedor(proveedor);

        }

        trabajo.setCalificacion(0);
        trabajo.setComentario("Sin comentario");
        trabajo.setRealizado(false);
        trabajo.setDescripcion(descripcion);

        trabajoRepositorio.save(trabajo);

    }

    @Transactional
    public void actualizar(String idTrabajo, String idUsuario, String idProveedor, boolean realizado, int calificacion, String comentario, String descripcion) throws MyException {

        validarTrabajo(idTrabajo, idProveedor, idUsuario, descripcion, comentario);

        Optional<Trabajo> t = trabajoRepositorio.findById(idTrabajo);

        if (t.isPresent()) {
            Trabajo trabajo = t.get();

            Optional<Usuario> user = usuarioRepositorio.findById(idUsuario);
            if (user.isPresent()) {
                Usuario usuario = user.get();
                trabajo.setUsuario(usuario);
            }

            Optional<Proveedor> prov = proveedorRepositorio.findById(idProveedor);
            if (prov.isPresent()) {
                Proveedor proveedor = prov.get();
                trabajo.setProveedor(proveedor);

            }
            trabajo.setCalificacion(calificacion);
            trabajo.setComentario(comentario);
            trabajo.setRealizado(realizado);
            trabajo.setDescripcion(descripcion);

            trabajoRepositorio.save(trabajo);
        }

    }

    public Trabajo getOne(String id) {
        return trabajoRepositorio.getOne(id);
    }

    @Transactional//(readOnly=True)
    public List<Trabajo> listarTrabajos() {

        List<Trabajo> trabajos = trabajoRepositorio.findAll();

        return trabajos;
    }

    @Transactional
    public void eliminar(String id) throws MyException {
        Trabajo trabajo = trabajoRepositorio.getById(id);
        trabajoRepositorio.delete(trabajo);
    }

    private void validar(String IdUsuario, String IdProveedor, String descripcion) throws MyException {

        if (IdUsuario == null || IdUsuario.isEmpty()) {
            throw new MyException("Debe ingresar el id del usuario");
        }
        
        if (IdProveedor == null || IdProveedor.isEmpty()) {
            throw new MyException("Debe ingresar el id del proveedor");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new MyException("Debe ingresar una descripción");
        }

    }

    private void validarTrabajo(String idTrabajo, String IdProveedor, String IdUsuario, String descripcion, String comentario) throws MyException {

        if (idTrabajo == null || idTrabajo.isEmpty()) {
            throw new MyException("Debe ingresar un comentario");
        }
        if (IdProveedor == null || IdProveedor.isEmpty()) {
            throw new MyException("Debe ingresar el id del proveedor");
        }
        if (IdUsuario == null || IdUsuario.isEmpty()) {
            throw new MyException("Debe ingresar el id del usuario");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MyException("Debe ingresar una descripción");
        }
        if (comentario == null || comentario.isEmpty()) {
            throw new MyException("Debe ingresar un comentario");
        }
    }

}
