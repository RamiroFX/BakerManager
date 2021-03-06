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
public class E_Marca {

    private int id;
    private String descripcion;

    public E_Marca() {
    }

    public E_Marca(int id, String descripcion) {
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
        return "Marca[" + getId() + ":" + getDescripcion() + "]";
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof E_Marca)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_Marca c = (E_Marca) o;

        // Compare the data members and return accordingly  
        return this.getId() == c.getId();
    }
}
