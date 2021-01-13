/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Usuario
 */
public class M_egreso_detalle {

    private Integer id_detalle;
    private Integer id_cabecera;
    private Integer id_producto;
    private M_producto producto;
    private Double precio;
    private Double iva_exenta;
    private Double iva_cinco;
    private Double iva_diez;
    private Double total;
    private Double cantidad;
    private Double descuento;
    String observacion;

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public M_egreso_detalle() {
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
    public Integer getId_producto() {
        return id_producto;
    }

    /**
     * @param id_producto the id_producto to set
     */
    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
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
    public Double getIva_exenta() {
        return iva_exenta;
    }

    /**
     * @param iva_exenta the iva_exenta to set
     */
    public void setIva_exenta(Double iva_exenta) {
        this.iva_exenta = iva_exenta;
    }

    /**
     * @return the iva_cinco
     */
    public Double getIva_cinco() {
        return iva_cinco;
    }

    /**
     * @param iva_cinco the iva_cinco to set
     */
    public void setIva_cinco(Double iva_cinco) {
        this.iva_cinco = iva_cinco;
    }

    /**
     * @return the iva_diez
     */
    public Double getIva_diez() {
        return iva_diez;
    }

    /**
     * @param iva_diez the iva_diez to set
     */
    public void setIva_diez(Double iva_diez) {
        this.iva_diez = iva_diez;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
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

    public M_producto getProducto() {
        return producto;
    }

    public double calcularSubTotal() {
        return getCantidad() * (getPrecio() - ((getPrecio() * getDescuento()) / 100));
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

}
