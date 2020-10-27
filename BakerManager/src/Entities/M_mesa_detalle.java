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

    private Integer idMesaDetalle;
    private M_mesa mesa;
    private M_producto producto;
    private Double cantidad, descuento, precio, total, exenta, iva5, iva10;
    private String observacion;

    public M_mesa_detalle() {
    }

    public M_mesa_detalle(M_facturaDetalle detalle) {
        this.idMesaDetalle = detalle.getIdFacturaDetalle();
        this.cantidad = detalle.getCantidad();
        this.descuento = detalle.getDescuento();
        this.observacion = detalle.getObservacion();
        this.precio = detalle.getPrecio();
        this.producto = detalle.getProducto();
    }

    public Integer getIdMesaDetalle() {
        return idMesaDetalle;
    }

    public void setIdMesaDetalle(Integer idMesaDetalle) {
        this.idMesaDetalle = idMesaDetalle;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getExenta() {
        return exenta;
    }

    public void setExenta(Double exenta) {
        this.exenta = exenta;
    }

    public Double getIva5() {
        return iva5;
    }

    public void setIva5(Double iva5) {
        this.iva5 = iva5;
    }

    public Double getIva10() {
        return iva10;
    }

    public void setIva10(Double iva10) {
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
