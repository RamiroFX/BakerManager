/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_movimientoContable {

    public static final int TIPO_COMPRA = 1, TIPO_VENTA = 2, TIPO_PAGO = 3,
            TIPO_COBRO = 4, TIPO_SALDO_INICIAL = 5;
    private int id;
    private int tipo;
    private E_facturaCabecera venta;
    private M_egreso_cabecera compra;
    private E_cuentaCorrienteCabecera cobro;
    private E_reciboPagoCabecera pago;

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
     * @return the venta
     */
    public E_facturaCabecera getVenta() {
        return venta;
    }

    /**
     * @param venta the venta to set
     */
    public void setVenta(E_facturaCabecera venta) {
        this.venta = venta;
    }

    /**
     * @return the compra
     */
    public M_egreso_cabecera getCompra() {
        return compra;
    }

    /**
     * @param compra the compra to set
     */
    public void setCompra(M_egreso_cabecera compra) {
        this.compra = compra;
    }

    /**
     * @return the cobro
     */
    public E_cuentaCorrienteCabecera getCobro() {
        return cobro;
    }

    /**
     * @param cobro the cobro to set
     */
    public void setCobro(E_cuentaCorrienteCabecera cobro) {
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

}
