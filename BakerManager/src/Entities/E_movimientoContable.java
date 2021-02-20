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
public class E_movimientoContable {

    public static final String STR_TIPO_COMPRA = "Factura compra",
            STR_TIPO_VENTA = "Factura",
            STR_TIPO_PAGO = "Recibo pago",
            STR_TIPO_COBRO = "Recibo",
            STR_TIPO_SALDO_INICIAL = "Saldo inicial",
            STR_TIPO_NOTA_CREDITO = "Nota de crédito",
            STR_TIPO_RETENCION_VENTA = "Retención",
            STR_TIPO_COBRO_ADELANTADO = "Cobro adelantado";
    public static final int TIPO_COMPRA = 1,
            TIPO_VENTA = 2,
            TIPO_PAGO = 3,
            TIPO_COBRO = 4,
            TIPO_SALDO_INICIAL = 5,
            TIPO_NOTA_CREDITO = 6,
            TIPO_RETENCION_VENTA = 7,
            TIPO_COBRO_ADELANTADO = 8;
    private int id;
    private int tipo;
    private String tipoDescripcion;
    private E_NotaCreditoCabecera notaCredito;
    private E_facturaSinPago venta;
    private M_egresoCabecera compra;
    private E_cuentaCorrienteDetalle cobro;
    private E_reciboPagoCabecera pago;
    private M_cliente clienteSaldoInicial;
    private E_retencionVenta retencionVenta;
    //AUX
    private Date fechaSaldoInicial;

    public E_movimientoContable() {
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
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the tipoDescripcion
     */
    public String getTipoDescripcion() {
        return tipoDescripcion;
    }

    /**
     * @param tipoDescripcion the tipoDescripcion to set
     */
    public void setTipoDescripcion(String tipoDescripcion) {
        this.tipoDescripcion = tipoDescripcion;
    }

    /**
     * @return the venta
     */
    public E_facturaSinPago getVenta() {
        return venta;
    }

    /**
     * @param venta the venta to set
     */
    public void setVenta(E_facturaSinPago venta) {
        this.venta = venta;
    }

    /**
     * @return the compra
     */
    public M_egresoCabecera getCompra() {
        return compra;
    }

    /**
     * @param compra the compra to set
     */
    public void setCompra(M_egresoCabecera compra) {
        this.compra = compra;
    }

    /**
     * @return the cobro
     */
    public E_cuentaCorrienteDetalle getCobro() {
        return cobro;
    }

    /**
     * @param cobro the cobro to set
     */
    public void setCobro(E_cuentaCorrienteDetalle cobro) {
        this.cobro = cobro;
    }

    /**
     * @return the pago
     */
    public E_reciboPagoCabecera getPago() {
        return pago;
    }

    /**
     * @param pago the pago to set
     */
    public void setPago(E_reciboPagoCabecera pago) {
        this.pago = pago;
    }

    public M_cliente getClienteSaldoInicial() {
        return clienteSaldoInicial;
    }

    public void setClienteSaldoInicial(M_cliente clienteSaldoInicial) {
        this.clienteSaldoInicial = clienteSaldoInicial;
    }

    public Date getFechaSaldoInicial() {
        return fechaSaldoInicial;
    }

    public void setFechaSaldoInicial(Date fechaSaldoInicial) {
        this.fechaSaldoInicial = fechaSaldoInicial;
    }

    public E_NotaCreditoCabecera getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(E_NotaCreditoCabecera notaCredito) {
        this.notaCredito = notaCredito;
    }

    /**
     * @return the retencionVenta
     */
    public E_retencionVenta getRetencionVenta() {
        return retencionVenta;
    }

    /**
     * @param retencionVenta the retencionVenta to set
     */
    public void setRetencionVenta(E_retencionVenta retencionVenta) {
        this.retencionVenta = retencionVenta;
    }

}
