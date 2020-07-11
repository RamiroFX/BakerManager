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
public class ProductoCategoria {

    private int id;
    private String descripcion;
    private ProductoCategoria padre;

    public ProductoCategoria() {
        padre = null;
    }

    public ProductoCategoria(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
        this.padre = null;
    }

    public ProductoCategoria(int id, String descripcion, ProductoCategoria padre) {
        this.id = id;
        this.descripcion = descripcion;
        this.padre = padre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return getId() + " - " + getDescripcion();
    }

    public ProductoCategoria getPadre() {
        return padre;
    }

    public void setPadre(ProductoCategoria padre) {
        this.padre = padre;
    }

    public boolean esPadre() {
        if (getPadre() != null) {
            if (getPadre().getId() > 0) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

}
