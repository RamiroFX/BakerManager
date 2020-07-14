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
        return getDescripcion();
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

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof ProductoCategoria)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        ProductoCategoria c = (ProductoCategoria) o;

        // Compare the data members and return accordingly  
        return this.getId() == c.getId();
    }

}
