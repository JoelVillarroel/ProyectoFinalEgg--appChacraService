

package com.GrupoE.WebAppServicios.entidades;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;



@Entity 
public class Trabajo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Proveedor proveedor;
    
    private boolean realizado;
    private int calificacion; // 0 siel usuario no ingreso aún una calificación. Del 1 al 5 una vez clasificado.
    private String comentario; //comentario de la calificación
    private String descripcion; //descripción del trabajo. Ej: Poda de arboles, Reparación de cañeria.
 

    public Trabajo() {
    }

    public Trabajo(Usuario usuario, Proveedor proveedor, boolean realizado, int calificacion, String comentario, String descripcion) {
        this.usuario = usuario;
        this.proveedor = proveedor;
        this.realizado = realizado;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
