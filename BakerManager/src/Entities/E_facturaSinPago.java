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
public class E_facturaSinPago {

    private int idCabecera;
    private int nroFactura;
    private int idCliente;
    private int monto;
    private int pago;
    private int saldo;
    private Date fecha;
    private String ruc;
    private String rucDiv;
    private String clienteEntidad;

    public E_facturaSinPago() {
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

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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
