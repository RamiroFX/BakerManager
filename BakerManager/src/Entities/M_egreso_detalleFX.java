/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Usuario
 */
public class M_egreso_detalleFX {

    private Integer id_detalle;
    private Integer id_cabecera;
    private String producto;
    private Double precio;
    private Integer iva_exenta;
    private Integer iva_cinco;
    private Integer iva_diez;
    private Integer total;
    private Double cantidad;
    private Double descuento;
    private String proveedor;
    private String observacion;
    private Date tiempo;

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public M_egreso_detalleFX() {
    }

    /**
     * @return the id_detalle
     */
    public Integer getId_detalle() {
        return id_detalle;
    }

    /**
     * @param id_detalle the id_detalle to set
     */
    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }

    /**
     * @return the id_cabecera
     */
    public Integer getId_cabecera() {
        return id_cabecera;
    }

    /**
     * @param id_cabecera the id_cabecera to set
     */
    public void setId_cabecera(Integer id_cabecera) {
        this.id_cabecera = id_cabecera;
    }

    /**
     * @return the id_producto
     */
    public String getProducto() {
        return producto;
    }

    /**
     * @param id_producto the id_producto to set
     */
    public void setProducto(String producto) {
        this.producto = producto;
    }

    /**
     * @return the precio
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * @return the iva_exenta
     */
    public Integer getIva_exenta() {
        return iva_exenta;
    }

    /**
     * @param iva_exenta the iva_exenta to set
     */
    public void setIva_exenta(Integer iva_exenta) {
        this.iva_exenta = iva_exenta;
    }

    /**
     * @return the iva_cinco
     */
    public Integer getIva_cinco() {
        return iva_cinco;
    }

    /**
     * @param iva_cinco the iva_cinco to set
     */
    public void setIva_cinco(Integer iva_cinco) {
        this.iva_cinco = iva_cinco;
    }

    /**
     * @return the iva_diez
     */
    public Integer getIva_diez() {
        return iva_diez;
    }

    /**
     * @param iva_diez the iva_diez to set
     */
    public void setIva_diez(Integer iva_diez) {
        this.iva_diez = iva_diez;
    }

    /**
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return the cantidad
     */
    public Double getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the descuento
     */
    public Double getDescuento() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    /**
     * @return the proveedor
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
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
