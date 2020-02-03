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
public class E_cuentaCorrienteDetalle {

    private int id;
    private int idCuentaCorrienteCabecera;
    private int idFacturaCabecera;
    private int nroFactura;
    private int nroRecibo;
    private double monto;
    private int nroCheque;//opcional
    private E_banco banco;//opcional
    private Date fechaCheque;//opcional
    private Date fechaDiferidaCheque;//opcional

    public E_cuentaCorrienteDetalle() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the idCuentaCorrienteCabecera
     */
    public int getIdCuentaCorrienteCabecera() {
        return idCuentaCorrienteCabecera;
    }

    /**
     * @param idCuentaCorrienteCabecera the idCuentaCorrienteCabecera to set
     */
    public void setIdCuentaCorrienteCabecera(int idCuentaCorrienteCabecera) {
        this.idCuentaCorrienteCabecera = idCuentaCorrienteCabecera;
    }

    /**
     * @return the idFacturaCabecera
     */
    public int getIdFacturaCabecera() {
        return idFacturaCabecera;
    }

    /**
     * @param idFacturaCabecera the idFacturaCabecera to set
     */
    public void setIdFacturaCabecera(int idFacturaCabecera) {
        this.idFacturaCabecera = idFacturaCabecera;
    }

    /**
     * @return the nroRecibo
     */
    public int getNroRecibo() {
        return nroRecibo;
    }

    /**
     * @param nroRecibo the nroRecibo to set
     */
    public void setNroRecibo(int nroRecibo) {
        this.nroRecibo = nroRecibo;
    }

    /**
     * @return the monto
     */
    public double getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * @return the nroCheque
     */
    public int getNroCheque() {
        return nroCheque;
    }

    /**
     * @param nroCheque the nroCheque to set
     */
    public void setNroCheque(int nroCheque) {
        this.nroCheque = nroCheque;
    }

    /**
     * @return the banco
     */
    public E_banco getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(E_banco banco) {
        this.banco = banco;
    }

    /**
     * @return the fechaCheque
     */
    public Date getFechaCheque() {
        return fechaCheque;
    }

    /**
     * @param fechaCheque the fechaCheque to set
     */
    public void setFechaCheque(Date fechaCheque) {
        this.fechaCheque = fechaCheque;
    }

    /**
     * @return the fechaDiferidaCheque
     */
    public Date getFechaDiferidaCheque() {
        return fechaDiferidaCheque;
    }

    /**
     * @param fechaDiferidaCheque the fechaDiferidaCheque to set
     */
    public void setFechaDiferidaCheque(Date fechaDiferidaCheque) {
        this.fechaDiferidaCheque = fechaDiferidaCheque;
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
}
