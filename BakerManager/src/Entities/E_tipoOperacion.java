/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro
 */
public class E_tipoOperacion {

    public static final int CONTADO = 1, CREDITO_30 = 2;

    private int id;
    private int duracion;
    private String descripcion;

    public E_tipoOperacion() {
    }

    public E_tipoOperacion(int id, int duracion, String descripcion) {
        this.id = id;
        this.duracion = duracion;
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
     * @return the duracion
     */
    public int getDuracion() {
        return duracion;
    }

    /**
     * @param duracion the duracion to set
     */
    public void setDuracion(int duracion) {
        this.duracion = duracion;
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
        if (!(o instanceof E_tipoOperacion)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_tipoOperacion c = (E_tipoOperacion) o;

        // Compare the data members and return accordingly  
        return this.getDescripcion().equals(c.getDescripcion());
    }

}
