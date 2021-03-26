/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_ajusteStockDetalle {

    private int id, idCabecera;
    private double cantidadVieja;
    private double cantidadNueva;
    private double cantidadMovimiento;
    private M_producto producto;
    private E_ajusteStockMotivo motivo;
    private Date tiempoRegistro;
    private String observacion;

    public E_ajusteStockDetalle() {
        this.producto = new M_producto();
        this.motivo = new E_ajusteStockMotivo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(int idCabecera) {
        this.idCabecera = idCabecera;
    }

    public double getCantidadVieja() {
        return cantidadVieja;
    }

    public void setCantidadVieja(double cantidadVieja) {
        this.cantidadVieja = cantidadVieja;
    }

    public double getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(double cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public M_producto getProducto() {
        return producto;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public E_ajusteStockMotivo getMotivo() {
        return motivo;
    }

    public void setMotivo(E_ajusteStockMotivo motivo) {
        this.motivo = motivo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getTiempoRegistro() {
        return tiempoRegistro;
    }

    public void setTiempoRegistro(Date tiempoRegistro) {
        this.tiempoRegistro = tiempoRegistro;
    }

    @Override
    public String toString() {
        return "AjusteStockDetalle{"
                + "id: " + getId() + ","
                + "tiempoRegistro: " + getTiempoRegistro() + ","
                + "cantVieja: " + getCantidadVieja() + ","
                + "cantNueva: " + getCantidadNueva() + ","
                + "Producto: " + getProducto().getDescripcion() + ","
                + "motivo: " + getMotivo().getDescripcion() + ","
                + "observacion: " + getObservacion()
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof E_ajusteStockDetalle)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members  
        E_ajusteStockDetalle c = (E_ajusteStockDetalle) o;

        // Compare the data members and return accordingly  
        return this.getId() == c.getId();
    }

    /**
     * @return the cantidadMovimiento
     */
    public double getCantidadMovimiento() {
        return cantidadMovimiento;
    }

    /**
     * @param cantidadMovimiento the cantidadMovimiento to set
     */
    public void setCantidadMovimiento(double cantidadMovimiento) {
        this.cantidadMovimiento = cantidadMovimiento;
    }
}
