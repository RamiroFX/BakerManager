/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_estadoPedido {

    public static final int PENDIENTE = 1, ENTREGADO = 2, CANCELADO = 3;

    public static final String PENDIENTE_STRING = "Pendiente",
            ENTREGADO_STRING = "Entregado",
            CANCELADO_STRING = "Cancelado";
    private int id;
    private String descripcion;

    public E_estadoPedido() {
    }

    public E_estadoPedido(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }

    public String toString2() {
        return "EstadoPedido[" + getId() + ":" + getDescripcion() + "]";
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof Estado)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_estadoPedido c = (E_estadoPedido) o;

        // Compare the data members and return accordingly  
        return this.getId() == c.getId();
    }

}
