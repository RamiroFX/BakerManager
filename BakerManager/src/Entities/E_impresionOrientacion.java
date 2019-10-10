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
public class E_impresionOrientacion {

    public static final int PORTRAIT = 1;
    public static final int LANDSCAPE = 2;
    public static final int REVERSE_LANDSCAPE = 3;

    private int id;
    private String descripcion;

    public E_impresionOrientacion() {
    }

    public E_impresionOrientacion(int id, String descripcion) {
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
        return getId() + "-" + getDescripcion();
    }

}
