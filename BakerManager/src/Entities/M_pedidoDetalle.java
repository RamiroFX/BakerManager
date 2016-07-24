/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_pedidoDetalle {

    private Integer idPedioDetalle, idPedido, precio, iva_exenta, iva_cinco, iva_diez, total;
    private Double cantidad, descuento;
    private String observacion;
    private M_producto producto;

    public M_pedidoDetalle() {
        this.producto = new M_producto();
    }

    public Integer getIdPedioDetalle() {
        return idPedioDetalle;
    }

    public void setIdPedioDetalle(Integer idPedioDetalle) {
        this.idPedioDetalle = idPedioDetalle;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getIva_exenta() {
        return iva_exenta;
    }

    public void setIva_exenta(Integer iva_exenta) {
        this.iva_exenta = iva_exenta;
    }

    public Integer getIva_cinco() {
        return iva_cinco;
    }

    public void setIva_cinco(Integer iva_cinco) {
        this.iva_cinco = iva_cinco;
    }

    public Integer getIva_diez() {
        return iva_diez;
    }

    public void setIva_diez(Integer iva_diez) {
        this.iva_diez = iva_diez;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
}
