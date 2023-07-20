package com.GrupoE.WebAppServicios.repositorios;

import com.GrupoE.WebAppServicios.entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor,String>{
    @Query("SELECT p FROM Proveedor p WHERE p.nombre = :nombre")
    public Proveedor buscarPorNombreProveedor(@Param("nombre")String nombre);
    @Query("SELECT p FROM Proveedor p WHERE p.apellido = :apellido")
    public Proveedor buscarPorApellidoProveedor(@Param("apellido")String apellido);
    /*@Query("SELECT p FROM Proveedor p WHERE p.nombre = :nombre")
    public List<Proveedor> buscarPorNombreProveedor(@Param("nombre")String nombre);*/
    /*@Query("SELECT p FROM Proveedor p WHERE p.apellido = :apellido")
    public List<Proveedor> buscarPorApellidoProveedor(@Param("apellido")String apellido);*/
}
