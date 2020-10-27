/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_facturaDetalleFX {

    private Integer idFacturaDetalle, nroFacura, idFacturaCabecera, idProducto;
    private Double cantidad, descuento, precio, total, exenta, iva5, iva10;
    private String productoDescripcion, observacion, clienteEntidad, condVenta;
    private M_producto producto;
    private Date tiempo;

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

    /**
     * @return the clienteEntidad
     */
    public String getClienteEntidad() {
        return clienteEntidad;
    }

    /**
     * @param clienteEntidad the clienteEntidad to set
     */
    public void setClienteEntidad(String clienteEntidad) {
        this.clienteEntidad = clienteEntidad;
    }

    /**
     * @return the tiempo
     */
    public Date getTiempo() {
        return tiempo;
    }

    /**
     * @param tiempo the tiempo to set
     */
    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getNroFacura() {
        return nroFacura;
    }

    public void setNroFacura(Integer nroFacura) {
        this.nroFacura = nroFacura;
    }

    public String getCondVenta() {
        return condVenta;
    }

    public void setCondVenta(String condVenta) {
        this.condVenta = condVenta;
    }
}
