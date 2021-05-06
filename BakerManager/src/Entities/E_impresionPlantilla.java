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
public class E_impresionPlantilla {

    public static final int TICKET = 1, FACTURA = 2, BOLETA = 3;

    private int id;
    private String descripcion;
    private E_impresionTipo tipo;
    private Estado estado;

    public E_impresionPlantilla() {
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

    public E_impresionTipo getTipo() {
        return tipo;
    }

    public void setTipo(E_impresionTipo tipo) {
        this.tipo = tipo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof E_impresionPlantilla)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_impresionPlantilla c = (E_impresionPlantilla) o;

        // Compare the data members and return accordingly  
        return this.getId() == c.getId();
    }

}
