/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.GrupoE.WebAppServicios.controladores;

import com.GrupoE.WebAppServicios.entidades.Proveedor;
import com.GrupoE.WebAppServicios.servicios.ProveedorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {
    @Autowired
    private ProveedorServicio proveedorServicio;
    
    
    @GetMapping("lista/fontanero")
    public String listarFontanero(ModelMap modelo){
        
        String descripcion="fontanero";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores",proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/electricista")
    public String listarElectricista(ModelMap modelo){
        String descripcion;
        descripcion="electricista";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/jardinero")
    public String listarJardinero(ModelMap modelo){
        String descripcion="jardinero";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/limpieza")
    public String listarLimpieza(ModelMap modelo){
        String descripcion="limpieza";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/pintura")
    public String listarPintura(ModelMap modelo){
        String descripcion="pintura";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/carpinteria")
    public String listarCarpinteria(ModelMap modelo){
        String descripcion="carpinteria";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/albañileria")
    public String listarAlbañileria(ModelMap modelo){
        String descripcion="albañileria";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/cerrajeria")
    public String listarCerrajeria(ModelMap modelo){
        String descripcion="cerrajeria";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/climatizacion")
    public String listarClimatizacion(ModelMap modelo){
        String descripcion="climatizacion";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/electronica")
    public String listarElectronica(ModelMap modelo){
        String descripcion="electronica";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/seguridad")
    public String listarSeguridad(ModelMap modelo){
        String descripcion="seguridad";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/mudanzas")
    public String listarMudanzas(ModelMap modelo){
        String descripcion="mudanzas";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/mecanica")
    public String listarMecanica(ModelMap modelo){
        String descripcion="mecanica";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/grafica")
    public String listarGrafica(ModelMap modelo){
        
        String descripcion="grafica";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
    @GetMapping("lista/software")
    public String listarSoftware(ModelMap modelo){
        String descripcion="software";
        List<Proveedor> proveedores= proveedorServicio.listarProveedoresPorDescripcion(descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_lista.html";
    }
   
    @GetMapping("/listaCompleta")
    public String listar(ModelMap modelo){
        List<Proveedor> proveedores= proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores",proveedores);
        return "proveedor_lista.html";
    }
}
