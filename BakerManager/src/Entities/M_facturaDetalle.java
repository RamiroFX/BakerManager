/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Administrador
 */
public class M_facturaDetalle {

    private Integer idFacturaDetalle, idFacturaCabecera, idProducto, precio, total, exenta, iva5, iva10;
    private Double cantidad, descuento;
    private String productoDescripcion, observacion;
    private M_producto producto;

    public Integer getIdFacturaDetalle() {
        return idFacturaDetalle;
    }

    public void setIdFacturaDetalle(Integer idFacturaDetalle) {
        this.idFacturaDetalle = idFacturaDetalle;
    }

    public Integer getIdFacturaCabecera() {
        return idFacturaCabecera;
    }

    public void setIdFacturaCabecera(Integer idFacturaCabecera) {
        this.idFacturaCabecera = idFacturaCabecera;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getProductoDescripcion() {
        return productoDescripcion;
    }

    public void setProductoDescripcion(String producto) {
        this.productoDescripcion = producto;
    }

    public M_producto getProducto() {
        return producto;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
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

    public Integer calcularSubTotal() {
        Integer Precio = getPrecio() - Math.round(Math.round(((getPrecio() * getDescuento()) / 100)));
        return Math.round(Math.round((getCantidad() * Precio)));
    }

    public Integer calcularTotal() {
        Integer totalAux = 0;
        Integer Precio = getPrecio() - Math.round(Math.round(((getPrecio() * getDescuento()) / 100)));
        totalAux = Math.round(Math.round((getCantidad() * Precio)));
        return totalAux;
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

    @Override
    public String toString() {
        return getProducto() + " " + getPrecio() + " " + getDescuento();
    }

}
