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
public class E_egresoSinPago {

    private int idCabecera;
    private int nroFactura;
    private int idProveedor;
    private int monto;
    private int pago;
    private int saldo;
    private Date fecha;
    private String ruc;
    private String rucDiv;
    private String proveedorEntidad;

    public E_egresoSinPago() {
    }

    /**
     * @return the idCabecera
     */
    public int getIdCabecera() {
        return idCabecera;
    }

    /**
     * @param idCabecera the idCabecera to set
     */
    public void setIdCabecera(int idCabecera) {
        this.idCabecera = idCabecera;
    }

    /**
     * @return the nroFactura
     */
    public int getNroFactura() {
        return nroFactura;
    }

    /**
     * @param nroFactura the nroFactura to set
     */
    public void setNroFactura(int nroFactura) {
        this.nroFactura = nroFactura;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    /**
     * @return the monto
     */
    public int getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(int monto) {
        this.monto = monto;
    }

    /**
     * @return the pago
     */
    public int getPago() {
        return pago;
    }

    /**
     * @param pago the pago to set
     */
    public void setPago(int pago) {
        this.pago = pago;
    }

    /**
     * @return the saldo
     */
    public int getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the ruc
     */
    public String getRuc() {
        return ruc;
    }

    /**
     * @param ruc the ruc to set
     */
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    /**
     * @return the rucDiv
     */
    public String getRucDiv() {
        return rucDiv;
    }

    /**
     * @param rucDiv the rucDiv to set
     */
    public void setRucDiv(String rucDiv) {
        this.rucDiv = rucDiv;
    }

    public String getProveedorEntidad() {
        return proveedorEntidad;
    }

    public void setProveedorEntidad(String proveedorEntidad) {
        this.proveedorEntidad = proveedorEntidad;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
