/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_mesa_detalle {

    private Integer idMesaDetalle, precio, total, exenta, iva5, iva10;
    private M_mesa mesa;
    private M_producto producto;
    private Double cantidad, descuento;
    private String observacion;

    public Integer getIdMesaDetalle() {
        return idMesaDetalle;
    }

    public void setIdMesaDetalle(Integer idMesaDetalle) {
        this.idMesaDetalle = idMesaDetalle;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getExenta() {
        return exenta;
    }

    public void setExenta(Integer exenta) {
        this.exenta = exenta;
    }

    public Integer getIva5() {
        return iva5;
    }

    public void setIva5(Integer iva5) {
        this.iva5 = iva5;
    }

    public Integer getIva10() {
        return iva10;
    }

    public void setIva10(Integer iva10) {
        this.iva10 = iva10;
    }

    public M_mesa getMesa() {
        return mesa;
    }

    public void setMesa(M_mesa mesa) {
        this.mesa = mesa;
    }

    public M_producto getProducto() {
        return producto;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
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
}
