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
public class E_NotaCreditoDetalle {

    private int id;
    private double cantidad, descuento;
    private double precio;
    //private E_NotaCreditoCabecera notaCreditoCabecera;
    private E_facturaDetalle facturaDetalle;
    private M_producto producto;
    private String observacion;

    public E_NotaCreditoDetalle() {
        ///this.notaCreditoCabecera = new E_NotaCreditoCabecera();
        this.facturaDetalle = new E_facturaDetalle();
        this.producto = new M_producto();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /*
    public E_NotaCreditoCabecera getNotaCreditoCabecera() {
        return notaCreditoCabecera;
    }

    public void setNotaCreditoCabecera(E_NotaCreditoCabecera notaCreditoCabecera) {
        this.notaCreditoCabecera = notaCreditoCabecera;
    }*/
    public E_facturaDetalle getFacturaDetalle() {
        return facturaDetalle;
    }

    public void setFacturaDetalle(E_facturaDetalle facturaDetalle) {
        this.facturaDetalle = facturaDetalle;
    }

    public M_producto getProducto() {
        return producto;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    /**
     * @return the descuento
     */
    public double getDescuento() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getSubTotal() {
        return getCantidad() * getPrecio() - ((getPrecio() * getDescuento()) / 100);
    }

    /**
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
