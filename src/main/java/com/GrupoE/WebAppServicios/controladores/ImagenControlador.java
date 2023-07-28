/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Imagen;
import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.entidades.Usuario;
import com.GrupoE.WebAppServicios.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    @Autowired
    ImagenRepositorio imagenServicio;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]>imagenUsuario(@PathVariable String id){
        Imagen imagenn = imagenServicio.getOne(id);
        byte[] imagen=imagenn.getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imagen, headers,HttpStatus.OK);
    }
    
    @GetMapping("/perfilProveedor/{id}")
    public ResponseEntity<byte[]> imagenProveedor(@PathVariable String id) {
        // Lógica para cargar la imagen del proveedor por su nombre de imagen

        Imagen imagenn = imagenServicio.getOne(id);
        byte[] imagen = imagenn.getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}
