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
public class E_facturaDetalle {

    private Integer idFacturaDetalle;
    private Double cantidad, descuento, precio;
    private String observacion;
    private M_producto producto;

    public E_facturaDetalle() {
    }

    public E_facturaDetalle(Integer idFacturaDetalle, Double precio, Double cantidad, Double descuento, String observacion, M_producto producto) {
        this.idFacturaDetalle = idFacturaDetalle;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.observacion = observacion;
        this.producto = producto;
    }

    public Integer getIdFacturaDetalle() {
        return idFacturaDetalle;
    }

    public void setIdFacturaDetalle(Integer idFacturaDetalle) {
        this.idFacturaDetalle = idFacturaDetalle;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public M_producto getProducto() {
        return producto;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public double calcularSubTotal() {
        return getCantidad() * (getPrecio() - ((getPrecio() * getDescuento()) / 100));
    }

    @Override
    public String toString() {
        return getCantidad() + "|" + getProducto().getDescripcion() + "|" + getPrecio();
    }

}
