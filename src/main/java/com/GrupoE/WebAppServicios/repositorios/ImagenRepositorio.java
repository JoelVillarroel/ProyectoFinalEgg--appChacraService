package com.GrupoE.WebAppServicios.repositorios;

import com.GrupoE.WebAppServicios.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String>{
    
}
