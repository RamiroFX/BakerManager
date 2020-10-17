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
    private E_formaPago formaPago;
    private E_tipoCheque tipoCheque;
    private E_cuentaCorrienteCabecera cuentaCorrienteCabecera;
    private E_reciboPagoCabecera reciboPagoCabecera;
    private E_facturaCabecera facturaVenta;
    private E_reciboTipoPago tipoPago;
    private int nroCheque;//opcional
    private E_banco banco;//opcional
    private Date fechaCheque;//opcional
    private Date fechaDiferidaCheque;//opcional

    public E_cuentaCorrienteDetalle() {
        this.formaPago = new E_formaPago();
        this.tipoCheque = new E_tipoCheque();
        this.facturaVenta = new E_facturaCabecera();
        this.cuentaCorrienteCabecera = new E_cuentaCorrienteCabecera();
        this.tipoPago = new E_reciboTipoPago();
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

    /**
     * @return the formaPago
     */
    public E_formaPago getFormaPago() {
        return formaPago;
    }

    /**
     * @param formaPago the formaPago to set
     */
    public void setFormaPago(E_formaPago formaPago) {
        this.formaPago = formaPago;
    }

    public E_formaPago calcularFormaPago() {
        if (getBanco() == null) {
            return new E_formaPago(E_formaPago.EFECTIVO, "Efectivo");
        } else {
            if (getBanco().getId() == 0) {
                return new E_formaPago(E_formaPago.EFECTIVO, "Efectivo");
            } else if (getNroCheque() == 0 && getFechaCheque() == null) {
                return new E_formaPago(E_formaPago.TARJETA, "Tarjeta");
            } else {
                return new E_formaPago(E_formaPago.CHEQUE, "Cheque");
            }
        }
    }

    /**
     * @return the tipoCheque
     */
    public E_tipoCheque getTipoCheque() {
        return tipoCheque;
    }

    /**
     * @param tipoCheque the tipoCheque to set
     */
    public void setTipoCheque(E_tipoCheque tipoCheque) {
        this.tipoCheque = tipoCheque;
    }

    public boolean esChequeDiferido() {
        System.out.println("esChequeDiferido().getTipoCheque().getId(): " + getTipoCheque().getId());
        System.out.println("esChequeDiferido().getTipoCheque().getDescripcion(): " + getTipoCheque().getDescripcion());
        if (getTipoCheque().getId() == E_tipoCheque.DIFERIDO) {
            System.out.println("esChequeDiferido()");
            return true;
        }
        return false;
    }

    /**
     * @return the facturaVenta
     */
    public E_facturaCabecera getFacturaVenta() {
        return facturaVenta;
    }

    /**
     * @param facturaVenta the facturaVenta to set
     */
    public void setFacturaVenta(E_facturaCabecera facturaVenta) {
        this.facturaVenta = facturaVenta;
    }

    /**
     * @return the cuentaCorrienteCabecera
     */
    public E_cuentaCorrienteCabecera getCuentaCorrienteCabecera() {
        return cuentaCorrienteCabecera;
    }

    /**
     * @param cuentaCorrienteCabecera the cuentaCorrienteCabecera to set
     */
    public void setCuentaCorrienteCabecera(E_cuentaCorrienteCabecera cuentaCorrienteCabecera) {
        this.cuentaCorrienteCabecera = cuentaCorrienteCabecera;
    }

    /**
     * @return the tipoPago
     */
    public E_reciboTipoPago getTipoPago() {
        return tipoPago;
    }

    /**
     * @param tipoPago the tipoPago to set
     */
    public void setTipoPago(E_reciboTipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    /**
     * @return the reciboPagoCabecera
     */
    public E_reciboPagoCabecera getReciboPagoCabecera() {
        return reciboPagoCabecera;
    }

    /**
     * @param reciboPagoCabecera the reciboPagoCabecera to set
     */
    public void setReciboPagoCabecera(E_reciboPagoCabecera reciboPagoCabecera) {
        this.reciboPagoCabecera = reciboPagoCabecera;
    }
}
