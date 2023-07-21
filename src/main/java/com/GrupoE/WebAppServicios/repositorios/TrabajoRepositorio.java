

package com.GrupoE.WebAppServicios.repositorios;
import com.GrupoE.WebAppServicios.entidades.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TrabajoRepositorio extends JpaRepository<Trabajo,String>{
    @Query("SELECT t FROM Trabajo t WHERE t.proveedor.nombre = :nombre")
    public Trabajo buscarPorProveedor(@Param("nombre")String nombre);
    
    @Query("SELECT t FROM Trabajo t WHERE t.calificacion = :calificacion")
    public Trabajo buscarPorCalificacion(@Param("calificacion")int calificacion);
    
    @Query("SELECT t FROM Trabajo t WHERE t.usuario.nombre = :nombre")
    public Trabajo buscarPorNombreUsuario(@Param("nombre")String nombre);
    
    @Query("SELECT t FROM Trabajo t WHERE t.proveedor.id = :id")
    public Trabajo buscarPorIdProveedor(@Param("id")String id);
    
    @Query("SELECT t FROM Trabajo t WHERE t.usuario.id = :id")
    public Trabajo buscarPorIdUsuario(@Param("id")String id);
    
}