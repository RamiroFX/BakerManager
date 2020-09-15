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
public class E_clienteProducto {

    private int id;
    private M_cliente cliente;
    private M_producto producto;
    private E_impuesto impuesto;
    private int precio;

    public E_clienteProducto() {
        this.cliente = new M_cliente();
        this.producto = new M_producto();
        this.impuesto = new E_impuesto();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public M_cliente getCliente() {
        return cliente;
    }

    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

    public M_producto getProducto() {
        return producto;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public E_impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(E_impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ClienteProducto["
                + "idCliente:" + cliente.getIdCliente()
                + ", idProducto:" + producto.getId()
                + ", idImpuesto:" + impuesto.getId()
                + ", precio:" + precio
                + "]";
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof E_clienteProducto)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_clienteProducto c = (E_clienteProducto) o;

        // Compare the data members and return accordingly  
        return this.getId() == c.getId();
    }
}
