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
public class E_produccionTipo {

    public static final int PRODUCTO_TERMINADO = 1, ROLLO = 2;
    public static final String PRODUCTO_TERMINADO_STRING = "Producto terminado", ROLLO_STRING = "Rollo";    

    private int id;
    private String descripcion;

    public E_produccionTipo() {
    }

    public E_produccionTipo(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof E_produccionTipo)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_produccionTipo c = (E_produccionTipo) o;

        // Compare the data members and return accordingly  
        return this.getDescripcion().equals(c.getDescripcion());
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
